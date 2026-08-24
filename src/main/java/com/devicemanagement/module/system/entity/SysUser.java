package com.devicemanagement.module.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/** 系统用户，与 Spring Security 的认证账号对应。 */
@Data
@TableName("sys_user")
@Schema(description = "系统用户")
public class SysUser {

    @TableId(type = IdType.AUTO)
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
    @Schema(description = "接收同步邮件的真实邮箱，空值则不发送邮件", example = "admin@example.com")
    private String email;
    @Schema(description = "创建时间", example = "2026-08-16 10:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /** 用户所属角色名称，非数据库字段，分页查询时手动填充。 */
    @TableField(exist = false)
    @Schema(description = "角色名称，多角色以顿号分隔", example = "管理员、运维人员")
    private String roleName;
}
