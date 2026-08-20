package com.devicemanagement.module.workorder.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "工单查询条件")
public class WorkOrderQueryDTO {
    /** 工单ID */
    @Schema(description = "工单ID", example = "1")
    private Long orderId;
    /** 工单标题 */
    @Schema(description = "工单标题", example = "xxx设备故障")
    private String orderTitle;
    /** 工单状态：1 未处理、2 处理中、3 待核验、4 核验完成、5 核验未通过 */
    @Schema(description = "工单状态", example = "1")
    private Integer orderStatus;
    /** 处理人ID */
    @Schema(description = "处理人ID", example = "1")
    private Long userId;
    /** 页码 */
    @Schema(description = "页码", example = "1")
    private Long pageNum = 1L;
    /** 每页条数 */
    @Schema(description = "每页条数", example = "10")
    private Long pageSize = 10L;
}
