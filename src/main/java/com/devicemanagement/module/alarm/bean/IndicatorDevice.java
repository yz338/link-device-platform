package com.devicemanagement.module.alarm.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName(value = "indicator_device")
@Schema(description = "告警指标-设备关联")
public class IndicatorDevice implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @Schema(description = "告警指标ID")
    private Long indicatorId;
    @Schema(description = "设备ID")
    private Long deviceId;
    private static final long serialVersionUID = 1L;
}