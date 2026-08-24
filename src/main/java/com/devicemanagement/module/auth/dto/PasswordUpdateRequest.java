package com.devicemanagement.module.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/** 修改本人密码的请求（需要校验原密码）。 */
@Data
@Schema(description = "个人密码修改请求")
public class PasswordUpdateRequest {
    @Schema(description = "原密码", required = true, example = "123456")
    @NotBlank(message = "原密码不能为空")
    private String oldPassword;

    @Schema(description = "新密码", required = true, example = "12345678")
    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 50, message = "密码长度应为6-50位")
    private String newPassword;
}
