package com.devicemanagement.module.workorder.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/** 处理工单请求：处理人提交处理方式与处理说明。 */
@Data
public class ProcessWorkOrderRequest {

    @NotNull(message = "工单ID不能为空")
    @Schema(description = "工单ID", example = "1")
    private Long orderId;

    @NotBlank(message = "处理方式不能为空")
    @Schema(description = "处理方式", example = "更换配件")
    private String method;

    @NotBlank(message = "处理说明不能为空")
    @Schema(description = "处理说明", example = "更换了坏掉的配件")
    private String processDescription;
}
