package com.devicemanagement.module.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;

/** 新增角色时的编码和名称。 */
@Data
@Schema(description = "新增角色请求")
public class RoleCreateRequest {
    @NotBlank(message = "角色编码不能为空")
    @Size(max = 50, message = "角色编码长度不能超过50")
    @Schema(description = "角色编码", example = "ADMIN")
    private String roleCode;

    @NotBlank(message = "角色名称不能为空")
    @Size(max = 50, message = "角色名称长度不能超过50")
    @Schema(description = "角色名称", example = "系统管理员")
    private String roleName;
}
