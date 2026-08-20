package com.devicemanagement.module.workorder.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class CreateWorkOrderRequest {
    @NotNull(message = "告警ID不能为空")
    @Schema(description = "告警ID", example = "1")
    private Long alarmId;

    @NotNull(message = "处理人不能为空")
    @Schema(description = "处理人ID", example = "1")
    private Long userId;

    @NotBlank(message = "工单标题不能为空")
    @Schema(description = "工单标题", example = "xx设备报警")
    private String orderTitle;

    @Future(message = "截止时间必须晚于当前时间")
    @Schema(description = "截止时间", example = "2026-08-20 10:53:00")
    private LocalDateTime deadline;

    @NotBlank(message = "工单描述不能为空")
    @Schema(description = "工单描述", example = "xx设备报警,请及时处理")
    private String orderDescription;
}

