package com.devicemanagement.module.alarm.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "告警明细")
public class AlarmDetailVO {
    @Schema(description = "告警ID")
    private Long id;
    @Schema(description = "告警设备")
    private String deviceName;
    @Schema(description = "设备ID")
    private Long deviceId;
    @Schema(description = "告警内容")
    private String alarmContent;
    @Schema(description = "设备种类")
    private String deviceCategory;
    @Schema(description = "设备地址")
    private String deviceAddress;
    @Schema(description = "告警时间")
    private String alarmTime;
    @Schema(description = "状态")
    private Integer status;
    @Schema(description = "处理人")
    private String handler;
}