package com.devicemanagement.module.system.controller;

import com.devicemanagement.common.Result;
import com.devicemanagement.module.system.dto.PermissionSaveRequest;
import com.devicemanagement.module.system.entity.SysPermission;
import com.devicemanagement.module.system.service.SystemPermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** 管理员维护系统可分配的权限字典。 */
@RestController
@RequestMapping("/api/system/permissions")
@RequiredArgsConstructor
@Tag(name = "权限管理", description = "权限字典的查询、新增、修改和删除")
public class SystemPermissionController {

    private final SystemPermissionService systemPermissionService;

    /** 返回权限字典；角色授权页面与权限管理页面均可调用。 */
    @GetMapping
    @Operation(summary = "查询权限列表")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<List<SysPermission>> listPermissions() {
        return Result.success(systemPermissionService.list());
    }

    /** 新增一项可分配权限。 */
    @PostMapping
    @Operation(summary = "新增权限")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<SysPermission> createPermission(@Valid @RequestBody PermissionSaveRequest request) {
        return Result.success(systemPermissionService.create(request));
    }

    /** 修改一项可分配权限。 */
    @PutMapping("/{permissionId}")
    @Operation(summary = "修改权限")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<SysPermission> updatePermission(@PathVariable Long permissionId,
                                                   @Valid @RequestBody PermissionSaveRequest request) {
        return Result.success(systemPermissionService.update(permissionId, request));
    }

    /** 删除一项可分配权限及其角色关联。 */
    @DeleteMapping("/{permissionId}")
    @Operation(summary = "删除权限")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> deletePermission(@PathVariable Long permissionId) {
        systemPermissionService.delete(permissionId);
        return Result.success(null);
    }
}
