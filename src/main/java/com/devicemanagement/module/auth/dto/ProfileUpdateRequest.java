package com.devicemanagement.module.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

/** 修改本人昵称和邮箱的请求。 */
@Data
@Schema(description = "个人资料修改请求")
public class ProfileUpdateRequest {
    @Schema(description = "用户昵称", example = "管理员")
    @Size(max = 50, message = "昵称长度不能超过50")
    private String nickname;

    @Schema(description = "邮箱（用于接收消息邮件）", example = "admin@example.com")
    @Size(max = 100, message = "邮箱长度不能超过100")
    @Email(message = "邮箱格式不正确")
    private String email;
}
