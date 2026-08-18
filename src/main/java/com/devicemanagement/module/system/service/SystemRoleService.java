package com.devicemanagement.module.system.service;

import com.devicemanagement.framework.redis.UserAuthorityCacheService;
import com.devicemanagement.common.exception.BusinessException;
import com.devicemanagement.module.system.dto.PermissionIdsRequest;
import com.devicemanagement.module.system.dto.RoleCreateRequest;
import com.devicemanagement.module.system.entity.SysRole;
import com.devicemanagement.module.system.mapper.SysRoleMapper;
import com.devicemanagement.module.system.mapper.SysRolePermissionMapper;
import com.devicemanagement.module.system.mapper.SysUserMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** 角色、权限字典及角色授权的基础服务。 */
@Service
@RequiredArgsConstructor
public class SystemRoleService {

    private final SysRoleMapper sysRoleMapper;
    private final SysRolePermissionMapper sysRolePermissionMapper;
    private final SysUserMapper sysUserMapper;
    private final UserAuthorityCacheService userAuthorityCacheService;

    /** 查询所有角色，供用户角色分配和角色管理页面使用。 */
    public List<SysRole> listRoles() {
        return sysRoleMapper.selectList(Wrappers.<SysRole>lambdaQuery().orderByAsc(SysRole::getId));
    }

    /** 查询角色当前拥有的权限 ID，供前端分配权限弹窗回显。 */
    public List<Long> listPermissionIds(Long roleId) {
        if (sysRoleMapper.selectById(roleId) == null) {
            throw new BusinessException("角色不存在");
        }
        return sysRolePermissionMapper.selectPermissionIdsByRoleId(roleId);
    }

    /** 创建角色，并阻止重复的角色编码。 */
    public SysRole create(RoleCreateRequest request) {
        boolean exists = sysRoleMapper.exists(Wrappers.<SysRole>lambdaQuery()
                .eq(SysRole::getRoleCode, request.getRoleCode()));
        if (exists) {
            throw new BusinessException("角色编码已存在");
        }
        SysRole role = new SysRole();
        role.setRoleCode(request.getRoleCode());
        role.setRoleName(request.getRoleName());
        sysRoleMapper.insert(role);
        return role;
    }

    /** 覆盖角色的权限关联，并清除该角色下全部用户的权限缓存。 */
    @Transactional
    public void assignPermissions(Long roleId, PermissionIdsRequest request) {
        if (sysRoleMapper.selectById(roleId) == null) {
            throw new BusinessException("角色不存在");
        }
        sysRolePermissionMapper.deleteByRoleId(roleId);
        request.getPermissionIds().forEach(permissionId -> sysRolePermissionMapper.insertRelation(roleId, permissionId));
        // 一个角色可能分配给多个用户，事务提交后逐个删除缓存以确保下次请求得到新权限。
        sysUserMapper.selectUserIdsByRoleId(roleId)
                .forEach(userAuthorityCacheService::invalidateAfterCommit);
    }
}
