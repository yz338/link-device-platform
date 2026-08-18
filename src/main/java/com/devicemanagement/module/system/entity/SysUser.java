package com.devicemanagement.module.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/** 系统用户，与 Spring Security 的认证账号对应。 */
@Data
@TableName("sys_user")
@Schema(description = "系统用户")
public class SysUser {

    @TableId
    @Schema(description = "用户主键", example = "1")
    private Long id;
    @Schema(description = "登录用户名", example = "admin")
    private String username;

    /** 密码仅在认证时使用，任何接口响应都不返回。 */
    @JsonIgnore
    @Schema(description = "加密后的密码", accessMode = Schema.AccessMode.WRITE_ONLY)
    private String password;
    @Schema(description = "用户昵称", example = "管理员")
    private String nickname;
    @Schema(description = "是否启用", example = "true")
    private Boolean enabled;
    @Schema(description = "创建时间", example = "2026-08-16T10:00:00")
    private LocalDateTime createdAt;
}
