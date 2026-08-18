package com.devicemanagement.module.system.service;

import com.devicemanagement.framework.redis.UserAuthorityCacheService;
import com.devicemanagement.common.exception.BusinessException;
import com.devicemanagement.module.system.dto.PermissionSaveRequest;
import com.devicemanagement.module.system.entity.SysPermission;
import com.devicemanagement.module.system.mapper.SysPermissionMapper;
import com.devicemanagement.module.system.mapper.SysRolePermissionMapper;
import com.devicemanagement.module.system.mapper.SysUserMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** 权限字典的增删改查，以及权限变化后的缓存同步。 */
@Service
@RequiredArgsConstructor
public class SystemPermissionService {

    private final SysPermissionMapper sysPermissionMapper;
    private final SysRolePermissionMapper sysRolePermissionMapper;
    private final SysUserMapper sysUserMapper;
    private final UserAuthorityCacheService userAuthorityCacheService;

    /** 查询全部权限，供权限管理与角色授权页面使用。 */
    public List<SysPermission> list() {
        return sysPermissionMapper.selectList(Wrappers.<SysPermission>lambdaQuery()
                .orderByAsc(SysPermission::getPermissionCode));
    }

    /** 新增权限编码，并阻止重复的权限编码。 */
    public SysPermission create(PermissionSaveRequest request) {
        if (existsByCode(request.getPermissionCode(), null)) {
            throw new BusinessException("权限编码已存在");
        }
        SysPermission permission = new SysPermission();
        permission.setPermissionCode(request.getPermissionCode());
        permission.setPermissionName(request.getPermissionName());
        sysPermissionMapper.insert(permission);
        return permission;
    }

    /** 修改权限编码或名称，并让已拥有该权限的用户重新加载权限缓存。 */
    @Transactional
    public SysPermission update(Long permissionId, PermissionSaveRequest request) {
        SysPermission permission = getPermission(permissionId);
        if (existsByCode(request.getPermissionCode(), permissionId)) {
            throw new BusinessException("权限编码已存在");
        }
        List<Long> userIds = sysUserMapper.selectUserIdsByPermissionId(permissionId);
        permission.setPermissionCode(request.getPermissionCode());
        permission.setPermissionName(request.getPermissionName());
        sysPermissionMapper.updateById(permission);
        invalidateUsersAfterCommit(userIds);
        return permission;
    }

    /** 删除权限及其角色关联，避免角色权限关联表的外键约束阻止删除。 */
    @Transactional
    public void delete(Long permissionId) {
        getPermission(permissionId);
        List<Long> userIds = sysUserMapper.selectUserIdsByPermissionId(permissionId);
        sysRolePermissionMapper.deleteByPermissionId(permissionId);
        sysPermissionMapper.deleteById(permissionId);
        invalidateUsersAfterCommit(userIds);
    }

    /** 根据主键查询权限，不存在时返回明确业务提示。 */
    private SysPermission getPermission(Long permissionId) {
        SysPermission permission = sysPermissionMapper.selectById(permissionId);
        if (permission == null) {
            throw new BusinessException("权限不存在");
        }
        return permission;
    }

    /** 判断权限编码是否已被其他权限使用。 */
    private boolean existsByCode(String permissionCode, Long excludedId) {
        return sysPermissionMapper.exists(Wrappers.<SysPermission>lambdaQuery()
                .eq(SysPermission::getPermissionCode, permissionCode)
                .ne(excludedId != null, SysPermission::getId, excludedId));
    }

    /** 在事务提交后删除受影响用户的权限缓存。 */
    private void invalidateUsersAfterCommit(List<Long> userIds) {
        userIds.forEach(userAuthorityCacheService::invalidateAfterCommit);
    }
}
