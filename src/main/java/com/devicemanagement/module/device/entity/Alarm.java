package com.devicemanagement.module.device.entity;

import java.util.Date;
import lombok.Data;

/**
 * alarm
 */
@Data
public class Alarm {
    /**
     * 告警ID
     */
    private Long id;

    /**
     * 告警设备
     */
    private String alarmDevice;

    /**
     * 设备ID
     */
    private Long deviceId;

    /**
     * 告警内容
     */
    private String alarmContent;

    /**
     * 设备种类
     */
    private String deviceCategory;

    /**
     * 告警时间
     */
    private Date alarmTime;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 处理人
     */
    private String handler;
}