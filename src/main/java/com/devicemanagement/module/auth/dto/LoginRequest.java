package com.devicemanagement.module.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;

/** 登录请求，只保留认证闭环所需的四个字段。 */
@Data
@Schema(description = "账号登录请求")
public class LoginRequest {
    @NotBlank(message = "用户名不能为空")
    @Size(max = 50, message = "用户名长度不能超过50")
    @Schema(description = "登录用户名", example = "admin")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(max = 50, message = "密码长度不能超过50")
    @Schema(description = "登录密码", example = "123456")
    private String password;

    @NotBlank(message = "验证码编号不能为空")
    @Schema(description = "验证码唯一编号")
    private String captchaId;

    @NotBlank(message = "验证码不能为空")
    @Schema(description = "验证码文本", example = "1234")
    private String captchaCode;
}
