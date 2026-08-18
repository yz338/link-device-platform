package com.devicemanagement.module.system.controller;

import com.devicemanagement.common.Result;
import com.devicemanagement.module.system.dto.RoleIdsRequest;
import com.devicemanagement.module.system.dto.UserCreateRequest;
import com.devicemanagement.module.system.dto.UserEnabledRequest;
import com.devicemanagement.module.system.entity.SysUser;
import com.devicemanagement.module.system.service.SystemUserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/** 管理员维护系统账号；真正的权限控制由 @PreAuthorize 完成。 */
@RestController
@RequestMapping("/api/system/users")
@RequiredArgsConstructor
@Validated
@Tag(name = "用户管理", description = "系统用户查询、创建、启停和角色分配")
public class SystemUserController {

    private final SystemUserService systemUserService;

    /** 按关键字分页查询系统用户。 */
    @GetMapping
    @Operation(summary = "分页查询用户")
    @PreAuthorize("hasAuthority('system:user:list')")
    public Result<Page<SysUser>> page(@RequestParam(defaultValue = "1") @Min(value = 1, message = "页码必须大于0") long pageNum,
                                      @RequestParam(defaultValue = "10") @Min(value = 1, message = "每页条数必须大于0")
                                      @Max(value = 100, message = "每页条数不能超过100") long pageSize,
                                      @RequestParam(required = false) String keyword) {
        return Result.success(systemUserService.page(pageNum, pageSize, keyword));
    }

    /** 创建一个初始启用状态的系统用户。 */
    @PostMapping
    @Operation(summary = "新增用户")
    @PreAuthorize("hasAuthority('system:user:add')")
    public Result<SysUser> create(@Valid @RequestBody UserCreateRequest request) {
        return Result.success(systemUserService.create(request));
    }

    /** 启用或禁用指定用户。 */
    @PutMapping("/{userId}/enabled")
    @Operation(summary = "修改用户启用状态")
    @PreAuthorize("hasAuthority('system:user:update')")
    public Result<Void> updateEnabled(@PathVariable Long userId, @Valid @RequestBody UserEnabledRequest request) {
        systemUserService.updateEnabled(userId, request);
        return Result.success(null);
    }

    /** 以覆盖方式为用户分配角色。 */
    @PutMapping("/{userId}/roles")
    @Operation(summary = "分配用户角色")
    @PreAuthorize("hasAuthority('system:user:assign-role')")
    public Result<Void> assignRoles(@PathVariable Long userId, @Valid @RequestBody RoleIdsRequest request) {
        systemUserService.assignRoles(userId, request);
        return Result.success(null);
    }
}
