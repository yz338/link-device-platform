package com.devicemanagement.module.alarm.vo;

import com.devicemanagement.module.alarm.bean.AlarmIndicator;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Schema(description = "告警详情VO")
public class AlarmVO {
    @Schema(description = "告警ID")
    private Long id;
    @Schema(description = "告警设备")
    private String alarmDevice;
    @Schema(description = "设备ID")
    private Long deviceId;
    @Schema(description = "告警内容")
    private String alarmContent;
    @Schema(description = "设备种类")
    private String deviceCategory;
    @Schema(description = "告警时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date alarmTime;
    @Schema(description = "状态")
    private Integer status;
    @Schema(description = "处理人")
    private String handler;
    @Schema(description = "告警指标")
    private AlarmIndicator alarmIndicator;
    @Schema(description = "推送数据（最新前10条）")
    private List<?> data;
}