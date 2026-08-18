package com.devicemanagement.module.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.Data;

/** 启用或禁用账号的状态请求。 */
@Data
@Schema(description = "用户启用状态请求")
public class UserEnabledRequest {
    @NotNull(message = "启用状态不能为空")
    @Schema(description = "是否启用", example = "true")
    private Boolean enabled;
}
