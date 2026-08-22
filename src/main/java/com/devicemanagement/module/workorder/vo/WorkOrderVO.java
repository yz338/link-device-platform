package com.devicemanagement.module.workorder.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Schema(description = "工单VO")
public class WorkOrderVO {
    @Schema(description = "工单ID", example = "1")
    private String orderId;
    @Schema(description = "工单标题", example = "xxx设备故障")
    private String orderTitle;
    @Schema(description = "设备名称", example = "xxx设备")
    private String deviceName;
    @Schema(description = "处理人", example = "老王")
    private String userName;
    @Schema(description = "处理人用户ID", example = "2")
    private Long userId;
    @Schema(description = "关联告警ID", example = "1001")
    private Long alarmId;
    @Schema(description = "工单描述", example = "xxx设备故障，请及时处理")
    private String orderDescription;
    @Schema(description = "告警内容", example = "设备出现问题")
    private String alarmContent;
    @Schema(description = "截止时间", example = "2025-08-16T10:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date deadline;
    /** 工单状态：1 未处理、2 处理中、3 待核验、4 核验完成、5 核验未通过 */
    @Schema(description = "工单状态", example = "1")
    private Integer orderStatus;
    /** 处理方式 */
    @Schema(description = "处理方式", example = "更换配件")
    private String method;
    /** 处理说明 */
    @Schema(description = "处理说明", example = "更换了坏掉的配件")
    private String processDescription;
    /** 核验结果：0.未通过、1.通过 */
    @Schema(description = "核验结果", example = "1")
    private Integer verificationResult;
    @Schema(description = "核验说明", example = "核验通过，xx设备已恢复正常")
    private String verificationDescription;
    /** 处理完成图片路径列表 */
    @Schema(description = "处理图片路径列表")
    private List<String> images;
    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
