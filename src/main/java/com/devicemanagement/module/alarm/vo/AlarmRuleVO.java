package com.devicemanagement.module.alarm.vo;

import com.devicemanagement.module.alarm.bean.AlarmRuleCondition;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Schema(description = "告警规则VO")
public class AlarmRuleVO {

    @Schema(description = "规则ID")
    private Long id;

    @Schema(description = "告警设备")
    private String alarmDevice;

    @Schema(description = "关联告警指标ID")
    private Long alarmIndicatorId;

    @Schema(description = "告警通知频率（小时/次）")
    private Integer notifyInterval;

    @Schema(description = "顶层条件关联关系")
    private String topConditionRelation;

    @Schema(description = "规则关联关系")
    private String ruleRelation;

    @Schema(description = "关联设备类别ID")
    private Long deviceCategoryId;

    @Schema(description = "设备类别名称")
    private String deviceCategoryName;

    @Schema(description = "告警指标名称")
    private String alarmIndicatorName;

    @Schema(description = "启用状态")
    private Byte status;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @Schema(description = "规则条件列表")
    private List<AlarmRuleCondition> conditions;
}