package com.devicemanagement.module.workorder.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "工单统计结果")
public class WorkOrderStatsVO {

    @Schema(description = "工单总数")
    private long totalOrders;

    @Schema(description = "核验未通过次数")
    private long failedVerificationCount;
}
