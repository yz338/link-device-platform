package com.devicemanagement.module.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;

/** 管理员新增账号时的最小信息。 */
@Data
@Schema(description = "新增系统用户请求")
public class UserCreateRequest {
    @NotBlank(message = "用户名不能为空")
    @Size(max = 50, message = "用户名长度不能超过50")
    @Schema(description = "用户名", example = "zhangsan")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(max = 50, message = "密码长度不能超过50")
    @Schema(description = "初始密码", example = "123456")
    private String password;

    @NotBlank(message = "昵称不能为空")
    @Size(max = 50, message = "昵称长度不能超过50")
    @Schema(description = "用户昵称", example = "张三")
    private String nickname;

    @Size(max = 100, message = "邮箱长度不能超过100")
    @Schema(description = "接收通知的邮箱", example = "zhangsan@example.com")
    private String email;
}
