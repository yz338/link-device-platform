package com.devicemanagement.module.alarm.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Schema(description = "告警指标信息VO")
public class AlarmIndicatorVO {
    @Schema(description = "告警指标ID")
    private Long id;
    @Schema(description = "告警指标名称")
    private String indicatorName;
    @Schema(description = "设备名称（单设备场景兼容字段）")
    private String deviceName;
    @Schema(description = "作用设备名称列表（indicator_device 关联的多设备）")
    private List<String> deviceNames;
    @Schema(description = "指标描述")
    private String description;
    @Schema(description = "指标启用状态")
    private Byte status;
    @Schema(description = "指标创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @Schema(description = "关联设备类别名")
    private String deviceCategoryName;
}
