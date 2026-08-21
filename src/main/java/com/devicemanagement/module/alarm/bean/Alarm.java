package com.devicemanagement.module.alarm.bean;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * alarm
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@TableName("alarm")
@Schema(description = "告警对象")
public class Alarm {
    /**
     * 告警ID
     */
    @Schema(description = "告警ID", example = "1")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 告警设备
     */
    @Schema(description = "告警设备", example = "温湿度传感器A")
    private String alarmDevice;

    /**
     * 设备ID
     */
    @Schema(description = "设备ID", example = "1")
    private Long deviceId;

    /**
     * 告警内容
     */
    @Schema(description = "告警内容", example = "温度过高")
    private String alarmContent;

    /**
     * 设备种类
     */
    @Schema(description = "设备种类", example = "温湿度传感器")
    private String deviceCategory;

    /**
     * 告警时间
     */
    @Schema(description = "告警时间", example = "2021-04-15 12:00:00")
    private Date alarmTime;

    /**
     * 状态
     */
    @Schema(description = "状态：1=未处理 2=处理中 3=已处理", example = "1")
    private Integer status;

    /**
     * 处理人
     */
    @Schema(description = "处理人", example = "张三")
    private String handler;
    /** 关联告警指标ID */
    @Schema(description = "关联告警指标ID", example = "1")
    private Long alarmIndicatorId;
    @Schema(description = "告警类别：1=设备离线 2=数据异常")
    private Integer type;
}