package com.devicemanagement.module.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/** 权限码用于后端 @PreAuthorize 与前端按钮显示判断。 */
@Data
@TableName("sys_permission")
@Schema(description = "系统权限")
public class SysPermission {

    @TableId
    @Schema(description = "权限主键", example = "1")
    private Long id;
    @Schema(description = "权限编码", example = "system:user:list")
    private String permissionCode;
    @Schema(description = "权限名称", example = "查询用户")
    private String permissionName;
}
