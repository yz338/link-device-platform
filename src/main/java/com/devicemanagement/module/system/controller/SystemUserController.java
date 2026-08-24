package com.devicemanagement.module.system.controller;

import com.devicemanagement.common.Result;
import com.devicemanagement.module.system.dto.UserCreateRequest;
import com.devicemanagement.module.system.dto.UserEditRequest;
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

/** 管理员维护系统账号；统一用 hasRole('ADMIN')，仅 page 放开 USER（通知选接收人需要）。 */
@RestController
@RequestMapping("/api/system/users")
@RequiredArgsConstructor
@Validated
@Tag(name = "用户管理", description = "系统用户查询、创建、启停和角色分配")
public class SystemUserController {

    private final SystemUserService systemUserService;

    /** 按关键字分页查询系统用户；可选按 roleCode 过滤（USER/ADMIN）。通知选接收人场景需要 USER 也能调。 */
    @GetMapping
    @Operation(summary = "分页查询用户")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public Result<Page<SysUser>> page(@RequestParam(defaultValue = "1") @Min(value = 1, message = "页码必须大于0") long pageNum,
                                      @RequestParam(defaultValue = "10") @Min(value = 1, message = "每页条数必须大于0")
                                      @Max(value = 100, message = "每页条数不能超过100") long pageSize,
                                      @RequestParam(required = false) String keyword,
                                      @RequestParam(required = false) String roleCode) {
        return Result.success(systemUserService.page(pageNum, pageSize, keyword, roleCode));
    }

    /** 创建一个初始启用状态的系统用户。 */
    @PostMapping
    @Operation(summary = "新增用户")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<SysUser> create(@Valid @RequestBody UserCreateRequest request) {
        return Result.success(systemUserService.create(request));
    }

    /** 修改用户基础信息（昵称、邮箱）。 */
    @PutMapping("/{userId}")
    @Operation(summary = "编辑用户")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> edit(@PathVariable Long userId, @Valid @RequestBody UserEditRequest request) {
        systemUserService.edit(userId, request);
        return Result.success(null);
    }

    /** 管理员重置指定用户密码为新的明文密码（后台加密保存）。 */
    @PostMapping("/{userId}/reset-password")
    @Operation(summary = "重置用户密码")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> resetPassword(@PathVariable Long userId, @RequestParam String newPassword) {
        systemUserService.resetPassword(userId, newPassword);
        return Result.success(null);
    }

    /** 删除指定用户及其用户-角色关联，不能删除当前登录用户自身。 */
    @DeleteMapping("/{userId}")
    @Operation(summary = "删除用户")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> delete(@PathVariable Long userId) {
        systemUserService.delete(userId);
        return Result.success(null);
    }

    /** 启用或禁用指定用户。 */
    @PutMapping("/{userId}/enabled")
    @Operation(summary = "修改用户启用状态")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> updateEnabled(@PathVariable Long userId, @Valid @RequestBody UserEnabledRequest request) {
        systemUserService.updateEnabled(userId, request);
        return Result.success(null);
    }
}
