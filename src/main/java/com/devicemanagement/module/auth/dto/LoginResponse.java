package com.devicemanagement.module.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 登录和刷新接口统一返回的一组令牌。 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "登录或刷新令牌响应")
public class LoginResponse {
    @Schema(description = "访问业务接口的短期令牌")
    private String accessToken;
    @Schema(description = "换取新令牌的长期刷新令牌")
    private String refreshToken;
    @Schema(description = "Access Token 有效期，单位秒", example = "7200")
    private long expiresIn;
}
