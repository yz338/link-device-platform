package com.devicemanagement.module.auth.controller;

import com.devicemanagement.module.auth.dto.*;
import com.devicemanagement.module.auth.service.AuthService;
import com.devicemanagement.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/** 前后端分离认证接口。 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "认证管理", description = "验证码、登录、Token 刷新、退出登录和当前用户信息")
public class AuthController {

    private final AuthService authService;

    /** 获取一次性登录验证码。 */
    @PostMapping("/captcha")
    @Operation(summary = "获取验证码")
    public Result<CaptchaResponse> captcha() {
        return Result.success(authService.createCaptcha());
    }

    /** 校验账号、密码和验证码，并签发一组 Token。 */
    @PostMapping("/login")
    @Operation(summary = "账号登录")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return Result.success(authService.login(request));
    }

    /** 使用有效的 Refresh Token 换取新 Token。 */
    @PostMapping("/refresh")
    @Operation(summary = "刷新登录令牌")
    public Result<LoginResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return Result.success(authService.refresh(request));
    }

    /** 将当前 Access Token 加入黑名单，并可同时删除 Refresh Token。 */
    @PostMapping("/logout")
    @Operation(summary = "退出登录")
    public Result<Void> logout(@RequestHeader("Authorization") String authorization,
                               @RequestBody(required = false) RefreshTokenRequest request) {
        // 受 SecurityFilterChain 保护后，Authorization 一定来自已认证请求。
        authService.logout(authorization.substring(7), request);
        return Result.success(null);
    }

    /** 返回已认证用户的角色和权限码，供固定菜单页面使用。 */
    @GetMapping("/profile")
    @Operation(summary = "获取当前用户信息")
    public Result<UserProfileResponse> profile(Authentication authentication) {
        return Result.success(authService.profile(authentication.getName()));
    }

    /** 当前登录用户修改自己的昵称和邮箱。 */
    @PutMapping("/profile")
    @Operation(summary = "修改个人资料")
    public Result<Void> updateProfile(Authentication authentication,
                                      @Valid @RequestBody ProfileUpdateRequest request) {
        authService.updateProfile(authentication.getName(), request);
        return Result.success(null);
    }

    /** 当前登录用户修改自己的登录密码（需校验原密码）。 */
    @PutMapping("/password")
    @Operation(summary = "修改个人密码")
    public Result<Void> updatePassword(Authentication authentication,
                                       @Valid @RequestBody PasswordUpdateRequest request) {
        authService.updatePassword(authentication.getName(), request);
        return Result.success(null);
    }
}
