package com.devicemanagement.module.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;

/** 新增或修改权限字典时提交的字段。 */
@Data
@Schema(description = "权限保存请求")
public class PermissionSaveRequest {

    @NotBlank(message = "权限编码不能为空")
    @Size(max = 100, message = "权限编码不能超过100个字符")
    @Schema(description = "权限编码", example = "device:list")
    private String permissionCode;

    @NotBlank(message = "权限名称不能为空")
    @Size(max = 100, message = "权限名称不能超过100个字符")
    @Schema(description = "权限名称", example = "查看设备")
    private String permissionName;
}
