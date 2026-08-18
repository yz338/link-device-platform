package com.devicemanagement.module.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/** 角色是用户和权限之间的中间层，例如 ADMIN、USER。 */
@Data
@TableName("sys_role")
@Schema(description = "系统角色")
public class SysRole {

    @TableId
    @Schema(description = "角色主键", example = "1")
    private Long id;
    @Schema(description = "角色编码", example = "ADMIN")
    private String roleCode;
    @Schema(description = "角色名称", example = "系统管理员")
    private String roleName;
}
