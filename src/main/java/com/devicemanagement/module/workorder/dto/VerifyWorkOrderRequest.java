package com.devicemanagement.module.workorder.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/** 核验工单请求：管理员提交核验结果与核验说明。 */
@Data
public class VerifyWorkOrderRequest {

    @NotNull(message = "工单ID不能为空")
    @Schema(description = "工单ID", example = "1")
    private Long orderId;

    @NotNull(message = "核验结果不能为空")
    @Schema(description = "核验结果：0.未通过、1.通过", example = "1")
    private Integer verificationResult;

    @NotBlank(message = "核验说明不能为空")
    @Schema(description = "核验说明", example = "核验通过，xx设备已更换")
    private String verificationDescription;
}
