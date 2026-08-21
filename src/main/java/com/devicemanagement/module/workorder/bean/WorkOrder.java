package com.devicemanagement.module.workorder.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@TableName("work_order")
@Schema(description = "工单")
public class WorkOrder {
    @Schema(description = "工单ID", example = "1")
    @TableId(value = "order_id",type = IdType.AUTO)
    private Long orderId;
    @Schema(description = "工单标题", example = "xx设备报警")
    private String orderTitle;
    @Schema(description = "工单描述", example = "xx设备报警,请及时处理")
    private String orderDescription;
    @Schema(description = "工单处理人ID", example = "1")
    private Long userId;
    @Schema(description = "告警ID", example = "1")
    private Long alarmId;
    /** 工单状态：1 未处理、2 处理中、3 待核验、4 核验完成、5 核验未通过 */
    @Schema(description = "工单状态", example = "1")
    private Integer orderStatus;
    @Schema(description = "创建时间", example = "2026-08-20 10:53:00")
    private Date createTime;
    @Schema(description = "截止时间", example = "2026-08-20 10:53:00")
    private Date deadline;
    @Schema(description = "更新时间", example = "2026-08-20 10:53:00")
    private Date updateTime;
}
