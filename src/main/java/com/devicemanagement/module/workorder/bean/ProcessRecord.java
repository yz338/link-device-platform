package com.devicemanagement.module.workorder.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * process_record
 */
@Data
@TableName("process_record")
@Schema(description = "工单处理记录")
public class ProcessRecord {
    /**
     * 工单处理记录id
     */
    @TableId(type = IdType.AUTO)
    @Schema(description = "工单处理记录ID", example = "1")
    private Integer processId;

    /**
     * 工单id
     */
    @Schema(description = "工单ID", example = "1")
    private Integer orderId;

    /**
     * 处理人员id
     */
    @Schema(description = "处理人员ID", example = "1")
    private Integer userId;

    /**
     * 处理方式
     */
    @Schema(description = "处理方式", example = "更换配件")
    private String method;

    /**
     * 工单处理说明
     */
    @Schema(description = "工单处理说明", example = "更换了坏掉的配件")
    private String processDescription;

    /**
     * 工单处理完成时间
     */
    @Schema(description = "工单处理完成时间", example = "2026-08-20 10:53:00")
    private Date finishTime;

}