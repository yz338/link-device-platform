package com.devicemanagement.module.system.service;

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

    /** 按用户名或昵称分页查询系统用户。 */
    public Page<SysUser> page(long pageNum, long pageSize, String keyword) {
        return sysUserMapper.selectPage(Page.of(pageNum, pageSize), Wrappers.<SysUser>lambdaQuery()
                .like(keyword != null && !keyword.isBlank(), SysUser::getUsername, keyword)
                .or(keyword != null && !keyword.isBlank())
                .like(keyword != null && !keyword.isBlank(), SysUser::getNickname, keyword)
                .orderByDesc(SysUser::getId));
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
