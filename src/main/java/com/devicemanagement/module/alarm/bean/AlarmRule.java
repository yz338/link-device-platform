package com.devicemanagement.module.alarm.bean;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "alarm_rule")
@Schema(description = "告警规则")
public class AlarmRule implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "规则ID")
    private Long id;

    @Schema(description = "告警设备")
    private String alarmDevice;

    @Schema(description = "关联告警指标ID")
    private Long alarmIndicatorId;

    @Schema(description = "告警通知频率（小时/次）")
    private Integer notifyInterval;

    @Schema(description = "顶层条件关联关系：OR=或 AND=与")
    private String topConditionRelation;

    @Schema(description = "规则关联关系：OR=或 AND=与")
    private String ruleRelation;

    @Schema(description = "关联设备类别ID")
    private Long deviceCategoryId;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "规则创建时间")
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    private Date updateTime;

    @Schema(description = "规则创建人")
    private Long createBy;

    @Schema(description = "启用状态：0=禁用 1=启用")
    private Byte status;

    @TableLogic
    @Schema(description = "逻辑删除：0=未删除 1=已删除")
    private Byte isDeleted;

    private static final long serialVersionUID = 1L;

}