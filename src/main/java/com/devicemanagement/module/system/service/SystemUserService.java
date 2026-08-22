package com.devicemanagement.module.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.devicemanagement.framework.redis.UserAuthorityCacheService;
import com.devicemanagement.common.exception.BusinessException;
import com.devicemanagement.module.system.dto.RoleIdsRequest;
import com.devicemanagement.module.system.dto.UserCreateRequest;
import com.devicemanagement.module.system.dto.UserEnabledRequest;
import com.devicemanagement.module.system.entity.SysUser;
import com.devicemanagement.module.system.mapper.SysUserMapper;
import com.devicemanagement.module.system.mapper.SysUserRoleMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 用户管理的业务逻辑：账号创建、启停和角色分配。 */
@Service
@RequiredArgsConstructor
public class SystemUserService {

    private final SysUserMapper sysUserMapper;
    private final SysUserRoleMapper sysUserRoleMapper;
    private final UserAuthorityCacheService userAuthorityCacheService;
    private final PasswordEncoder passwordEncoder;

    /** 按用户名或昵称分页查询系统用户；可选按 roleCode 过滤（例如下发工单只需要运维人员 USER）。 */
    public Page<SysUser> page(long pageNum, long pageSize, String keyword, String roleCode) {
        boolean hasKeyword = keyword != null && !keyword.isBlank();
        if (roleCode == null || roleCode.isBlank()) {
            return sysUserMapper.selectPage(Page.of(pageNum, pageSize), Wrappers.<SysUser>lambdaQuery()
                    .like(hasKeyword, SysUser::getUsername, keyword)
                    .or(hasKeyword)
                    .like(hasKeyword, SysUser::getNickname, keyword)
                    .orderByDesc(SysUser::getId));
        }
        // 按角色过滤：通过 user_role + role 关联拿到用户 ID，再查用户
        String safeRoleCode = roleCode.replace("'", "");
        LambdaQueryWrapper<SysUser> qw = Wrappers.<SysUser>lambdaQuery()
                .inSql(SysUser::getId,
                        "SELECT ur.user_id FROM sys_user_role ur "
                                + "INNER JOIN sys_role r ON ur.role_id = r.id WHERE r.role_code = '"
                                + safeRoleCode + "'");
        if (hasKeyword) {
            qw.and(w -> w
                    .like(SysUser::getUsername, keyword)
                    .or()
                    .like(SysUser::getNickname, keyword));
        }
        qw.orderByDesc(SysUser::getId);
        return sysUserMapper.selectPage(Page.of(pageNum, pageSize), qw);
    }

    /** 检查用户是否持有指定角色（例如下发工单前校验处理人角色）。 */
    public boolean hasRole(Long userId, String roleCode) {
        if (userId == null || roleCode == null || roleCode.isBlank()) return false;
        return sysUserMapper.countUserWithRole(userId, roleCode) > 0;
    }

    /** 创建初始启用状态的账号，并将明文密码转换为 BCrypt 密文。 */
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
        return user;
    }

    /** 修改用户的启用状态并清除该用户权限缓存。 */
    public void updateEnabled(Long userId, UserEnabledRequest request) {
        SysUser user = getUser(userId);
        user.setEnabled(request.getEnabled());
        sysUserMapper.updateById(user);
        // 禁用用户后，旧 Token 请求会重新加载账号状态；同时清理权限缓存。
        userAuthorityCacheService.invalidateAfterCommit(userId);
    }

    /** 覆盖用户原有角色关联，并清除缓存使新权限生效。 */
    @Transactional
    public void assignRoles(Long userId, RoleIdsRequest request) {
        getUser(userId);
        sysUserRoleMapper.deleteByUserId(userId);
        request.getRoleIds().forEach(roleId -> sysUserRoleMapper.insertRelation(userId, roleId));
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
}
