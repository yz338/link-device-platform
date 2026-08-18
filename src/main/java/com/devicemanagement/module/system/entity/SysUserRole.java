package com.devicemanagement.module.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/** 用户与角色的关联实体，使用联合主键字段。 */
@Data
@TableName("sys_user_role")
@Schema(description = "用户与角色关联")
public class SysUserRole {

    @Schema(description = "用户 ID", example = "1")
    private Long userId;
    @Schema(description = "角色 ID", example = "1")
    private Long roleId;
}
