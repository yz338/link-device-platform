package com.devicemanagement.module.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/** 角色与权限的关联实体，角色权限变化时需要清理用户权限缓存。 */
@Data
@TableName("sys_role_permission")
@Schema(description = "角色与权限关联")
public class SysRolePermission {

    @Schema(description = "角色 ID", example = "1")
    private Long roleId;
    @Schema(description = "权限 ID", example = "1")
    private Long permissionId;
}
