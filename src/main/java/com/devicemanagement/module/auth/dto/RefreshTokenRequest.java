package com.devicemanagement.module.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.Data;

/** Refresh Token 只用于换取新的 Token，不直接访问业务接口。 */
@Data
@Schema(description = "Refresh Token 刷新或退出请求")
public class RefreshTokenRequest {
    @NotBlank(message = "Refresh Token不能为空")
    @Schema(description = "刷新令牌")
    private String refreshToken;
}
