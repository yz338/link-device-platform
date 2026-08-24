package com.devicemanagement.module.system.controller;

import com.devicemanagement.common.Result;
import com.devicemanagement.module.system.dto.PermissionIdsRequest;
import com.devicemanagement.module.system.dto.RoleCreateRequest;
import com.devicemanagement.module.system.entity.SysRole;
import com.devicemanagement.module.system.service.SystemRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** 管理员维护角色及角色可拥有的权限码。 */
@RestController
@RequestMapping("/api/system")
@RequiredArgsConstructor
@Tag(name = "角色与权限管理", description = "角色查询、角色创建和权限分配")
public class SystemRoleController {

    private final SystemRoleService systemRoleService;

    /** 返回所有可分配角色。 */
    @GetMapping("/roles")
    @Operation(summary = "查询角色列表")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<List<SysRole>> listRoles() {
        return Result.success(systemRoleService.listRoles());
    }

    /** 创建新的业务角色。 */
    @PostMapping("/roles")
    @Operation(summary = "新增角色")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<SysRole> createRole(@Valid @RequestBody RoleCreateRequest request) {
        return Result.success(systemRoleService.create(request));
    }

    /** 返回角色当前已拥有的权限 ID，用于前端复选框回显。 */
    @GetMapping("/roles/{roleId}/permission-ids")
    @Operation(summary = "查询角色已分配权限")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<List<Long>> listRolePermissionIds(@PathVariable Long roleId) {
        return Result.success(systemRoleService.listPermissionIds(roleId));
    }

    /** 以覆盖方式为角色分配权限码。 */
    @PutMapping("/roles/{roleId}/permissions")
    @Operation(summary = "分配角色权限")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> assignPermissions(@PathVariable Long roleId,
                                          @Valid @RequestBody PermissionIdsRequest request) {
        systemRoleService.assignPermissions(roleId, request);
        return Result.success(null);
    }

}
