package com.devicemanagement.module.workorder.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * verification_records
 */
@Data
@TableName("verification_records")
@Schema(description = "核验记录")
public class VerificationRecords{
    /**
     * 核验记录id
     */
    @TableId(type = IdType.AUTO)
    @Schema(description = "核验记录ID", example = "1")
    private Integer verificationId;

    /**
     * 工单id
     */
    @Schema(description = "工单ID", example = "1")
    private Integer orderId;

    /**
     * 核验结果
     */
    @Schema(description = "核验结果", example = "1")
    private Integer verificationResult;
    /**
     * 核验说明
     */
    @Schema(description = "核验说明", example = "核验通过，xx设备已恢复正常")
    private String verificationDescription;

    /**
     * 核验时间
     */
    @Schema(description = "核验时间", example = "2026-08-20 10:53:00")
    private Date verificationTime;

}