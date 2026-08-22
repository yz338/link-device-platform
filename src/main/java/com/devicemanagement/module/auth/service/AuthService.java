package com.devicemanagement.module.auth.service;

import com.devicemanagement.framework.config.JwtProperties;
import com.devicemanagement.framework.redis.LoginSecurityService;
import com.devicemanagement.framework.redis.RefreshTokenService;
import com.devicemanagement.framework.redis.TokenBlacklistService;
import com.devicemanagement.framework.redis.UserAuthorityCacheService;
import com.devicemanagement.framework.security.JwtUtil;
import com.devicemanagement.module.auth.dto.*;
import com.devicemanagement.common.exception.BusinessException;
import com.devicemanagement.module.system.entity.SysUser;
import com.devicemanagement.module.system.mapper.SysUserMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.List;

/** 聚合认证流程，Controller 不直接处理密码、Redis 或 JWT 细节。 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final JwtProperties jwtProperties;
    private final LoginSecurityService loginSecurityService;
    private final RefreshTokenService refreshTokenService;
    private final TokenBlacklistService tokenBlacklistService;
    private final UserAuthorityCacheService userAuthorityCacheService;
    private final SysUserMapper sysUserMapper;

    /** 创建用于登录的验证码。 */
    public CaptchaResponse createCaptcha() {
        return loginSecurityService.createCaptcha();
    }

    /** 完成验证码校验、密码认证和 Token 签发。 */
    public LoginResponse login(LoginRequest request) {
        loginSecurityService.verifyCaptcha(request.getCaptchaId(), request.getCaptchaCode());
        loginSecurityService.checkLocked(request.getUsername());
        try {
            authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken.unauthenticated(request.getUsername(), request.getPassword()));
            loginSecurityService.onLoginSuccess(request.getUsername());
            return createLoginResponse(request.getUsername());
        } catch (AuthenticationException exception) {
            loginSecurityService.onLoginFailure(request.getUsername());
            throw new BusinessException("用户名或密码错误");
        }
    }

    /** 消费旧 Refresh Token，并为仍启用的用户签发新 Token。 */
    public LoginResponse refresh(RefreshTokenRequest request) {
        String username = refreshTokenService.consume(request.getRefreshToken());
        SysUser user = findUser(username);
        // 用户在 Refresh Token 有效期间被禁用后，也不能继续换取新 Token。
        if (Boolean.FALSE.equals(user.getEnabled())) {
            throw new BusinessException("用户已被禁用");
        }
        return createLoginResponse(username);
    }

    /** 使当前 Access Token 和可选 Refresh Token 均失效。 */
    public void logout(String accessToken, RefreshTokenRequest request) {
        Claims claims = jwtUtil.parseToken(accessToken);
        tokenBlacklistService.add(claims);
        if (request != null) {
            refreshTokenService.delete(request.getRefreshToken());
        }
    }

    /** 组装前端固定页面所需的当前用户、角色和权限码。 */
    public UserProfileResponse profile(String username) {
        SysUser user = findUser(username);
        if (Boolean.FALSE.equals(user.getEnabled())) {
            throw new BusinessException("用户已被禁用");
        }
        List<String> authorityCodes = userAuthorityCacheService.getAuthorityCodes(user.getId());
        List<String> roles = authorityCodes.stream()
                .filter(code -> code.startsWith("ROLE_"))
                .map(code -> code.substring("ROLE_".length())).toList();
        List<String> permissions = authorityCodes.stream()
                .filter(code -> !code.startsWith("ROLE_")).toList();
        return new UserProfileResponse(user.getId(), user.getUsername(), user.getNickname(), roles, permissions);
    }

    /** 根据用户名统一创建 Access Token 和 Refresh Token。 */
    private LoginResponse createLoginResponse(String username) {
        return new LoginResponse(jwtUtil.generateAccessToken(username), refreshTokenService.create(username),
                jwtProperties.getExpireSeconds());
    }

    /** 查询用户，不存在时转换为业务异常。 */
    private SysUser findUser(String username) {
        SysUser user = sysUserMapper.selectOne(Wrappers.<SysUser>lambdaQuery()
                .eq(SysUser::getUsername, username));
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return user;
    }
}
