package com.devicemanagement.module.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.devicemanagement.framework.redis.UserAuthorityCacheService;
import com.devicemanagement.common.exception.BusinessException;
import com.devicemanagement.module.system.dto.UserCreateRequest;
import com.devicemanagement.module.system.dto.UserEditRequest;
import com.devicemanagement.module.system.dto.UserEnabledRequest;
import com.devicemanagement.module.system.entity.SysUser;
import com.devicemanagement.module.system.mapper.SysUserMapper;
import com.devicemanagement.module.system.mapper.SysUserRoleMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** 用户管理的业务逻辑：账号创建、编辑、启停、重置密码、删除和角色分配。 */
@Service
@RequiredArgsConstructor
public class SystemUserService {

    private final SysUserMapper sysUserMapper;
    private final SysUserRoleMapper sysUserRoleMapper;
    private final UserAuthorityCacheService userAuthorityCacheService;
    private final PasswordEncoder passwordEncoder;

    /**
     * 按用户名或昵称分页查询系统用户。
     * - 不传 roleCode 或传 "USER"：仅返回运维人员（用户管理页/下发工单选处理人场景）
     * - 传 "ADMIN"：仅返回管理员（通知发送人场景）
     * - 传 "ALL"：返回所有用户（统计/核验等页面需映射所有用户ID到昵称的场景）
     */
    public Page<SysUser> page(long pageNum, long pageSize, String keyword, String roleCode) {
        boolean hasKeyword = keyword != null && !keyword.isBlank();
        LambdaQueryWrapper<SysUser> qw = Wrappers.<SysUser>lambdaQuery();
        if (roleCode == null || roleCode.isBlank() || "USER".equalsIgnoreCase(roleCode)) {
            // 仅展示运维人员
            qw.inSql(SysUser::getId,
                    "SELECT ur.user_id FROM sys_user_role ur "
                            + "INNER JOIN sys_role r ON ur.role_id = r.id WHERE r.role_code = 'USER'");
        } else if ("ALL".equalsIgnoreCase(roleCode)) {
            // 不做角色过滤，返回所有用户（含管理员）
        } else {
            // 按指定角色过滤，例如下发工单场景显式传 ADMIN/USER
            String safeRoleCode = roleCode.replace("'", "");
            qw.inSql(SysUser::getId,
                    "SELECT ur.user_id FROM sys_user_role ur "
                            + "INNER JOIN sys_role r ON ur.role_id = r.id WHERE r.role_code = '"
                            + safeRoleCode + "'");
        }
        if (hasKeyword) {
            qw.and(w -> w
                    .like(SysUser::getUsername, keyword)
                    .or()
                    .like(SysUser::getNickname, keyword));
        }
        qw.orderByDesc(SysUser::getId);
        Page<SysUser> pageResult = sysUserMapper.selectPage(Page.of(pageNum, pageSize), qw);
        // 为每个用户填充角色名称字段
        if (pageResult.getRecords() != null && !pageResult.getRecords().isEmpty()) {
            for (SysUser user : pageResult.getRecords()) {
                List<String> roleNames = sysUserRoleMapper.selectRoleNamesByUserId(user.getId());
                if (roleNames != null && !roleNames.isEmpty()) {
                    user.setRoleName(String.join("、", roleNames));
                } else {
                    user.setRoleName("-");
                }
            }
        }
        return pageResult;
    }

    /** 检查用户是否持有指定角色（例如下发工单前校验处理人角色）。 */
    public boolean hasRole(Long userId, String roleCode) {
        if (userId == null || roleCode == null || roleCode.isBlank()) return false;
        return sysUserMapper.countUserWithRole(userId, roleCode) > 0;
    }

    /** 创建初始启用状态的运维账号，固定分配 USER 角色（role_id=2），将明文密码转换为 BCrypt 密文。 */
    @Transactional
    public SysUser create(UserCreateRequest request) {
        boolean exists = sysUserMapper.exists(Wrappers.<SysUser>lambdaQuery()
                .eq(SysUser::getUsername, request.getUsername()));
        if (exists) {
            throw new BusinessException("用户名已存在");
        }
        SysUser user = new SysUser();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNickname(request.getNickname());
        user.setEmail(request.getEmail());
        user.setEnabled(true);
        sysUserMapper.insert(user);
        // 固定分配运维人员（USER，role_id=2），管理员账号有且只有 1 个（admin），不允许通过新增接口产生
        sysUserRoleMapper.insertRelation(user.getId(), 2L);
        userAuthorityCacheService.invalidateAfterCommit(user.getId());
        return user;
    }

    /** 编辑用户基础信息（昵称、邮箱）。 */
    public void edit(Long userId, UserEditRequest request) {
        SysUser user = getUser(userId);
        user.setNickname(request.getNickname());
        user.setEmail(request.getEmail());
        sysUserMapper.updateById(user);
    }

    /** 管理员重置指定用户密码为新明文密码的加密形式，并清理该用户权限缓存。 */
    public void resetPassword(Long userId, String newPassword) {
        if (newPassword == null || newPassword.isBlank()) {
            throw new BusinessException("新密码不能为空");
        }
        if (newPassword.length() > 50) {
            throw new BusinessException("密码长度不能超过50");
        }
        SysUser user = getUser(userId);
        user.setPassword(passwordEncoder.encode(newPassword));
        sysUserMapper.updateById(user);
        userAuthorityCacheService.invalidateAfterCommit(userId);
    }

    /** 删除指定用户：先删除用户-角色关联，再删除用户主表记录；禁止删除当前登录用户自己；禁止删除管理员账号。 */
    @Transactional
    public void delete(Long userId) {
        SysUser user = getUser(userId);
        if (hasRole(userId, "ADMIN")) {
            throw new BusinessException("管理员账号有且只有一个，禁止删除");
        }
        Long currentUserId = getCurrentUserIdOrNull();
        if (currentUserId != null && currentUserId.equals(userId)) {
            throw new BusinessException("不能删除当前登录用户自己");
        }
        sysUserRoleMapper.deleteByUserId(userId);
        sysUserMapper.deleteById(userId);
        userAuthorityCacheService.invalidateAfterCommit(userId);
    }

    /** 修改用户的启用状态并清除该用户权限缓存；管理员账号禁止被禁用。 */
    public void updateEnabled(Long userId, UserEnabledRequest request) {
        SysUser user = getUser(userId);
        if (hasRole(userId, "ADMIN") && (request.getEnabled() == null || !request.getEnabled())) {
            throw new BusinessException("管理员账号禁止禁用");
        }
        user.setEnabled(request.getEnabled());
        sysUserMapper.updateById(user);
        // 禁用用户后，旧 Token 请求会重新加载账号状态；同时清理权限缓存。
        userAuthorityCacheService.invalidateAfterCommit(userId);
    }

    /** 按 ID 查询用户，不存在时返回明确业务提示。 */
    private SysUser getUser(Long userId) {
        SysUser user = sysUserMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return user;
    }

    /** 获取当前登录用户 ID；未登录或查询失败时返回 null。 */
    private Long getCurrentUserIdOrNull() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getName() != null) {
                SysUser sysUser = sysUserMapper.selectOne(Wrappers.<SysUser>lambdaQuery()
                        .eq(SysUser::getUsername, authentication.getName()));
                return sysUser == null ? null : sysUser.getId();
            }
        } catch (Exception ignored) {
        }
        return null;
    }
}
