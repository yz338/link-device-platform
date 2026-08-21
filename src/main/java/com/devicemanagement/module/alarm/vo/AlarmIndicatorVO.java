package com.devicemanagement.module.alarm.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "告警指标信息VO")
public class AlarmIndicatorVO {
    @Schema(description = "告警指标ID")
    private Long id;
    @Schema(description = "设备名称")
    private String deviceName;
    @Schema(description = "指标描述")
    private String description;
    @Schema(description = "指标启用状态")
    private Byte status;
    @Schema(description = "指标创建时间")
    private String createTime;
    @Schema(description = "关联设备类别名")
    private String deviceCategoryName;
}
