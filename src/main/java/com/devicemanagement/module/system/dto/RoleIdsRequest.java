package com.devicemanagement.module.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/** 为一个用户覆盖式分配角色。 */
@Data
@Schema(description = "用户角色分配请求")
public class RoleIdsRequest {
    @NotEmpty(message = "至少选择一个角色")
    @Schema(description = "需要分配的角色 ID 列表", example = "[1,2]")
    private List<Long> roleIds;
}
