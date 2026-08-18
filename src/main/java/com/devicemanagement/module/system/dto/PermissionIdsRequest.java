package com.devicemanagement.module.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/** 为一个角色覆盖式分配权限。 */
@Data
@Schema(description = "角色权限分配请求")
public class PermissionIdsRequest {
    @NotEmpty(message = "至少选择一个权限")
    @Schema(description = "需要分配的权限 ID 列表", example = "[1,2,3]")
    private List<Long> permissionIds;
}
