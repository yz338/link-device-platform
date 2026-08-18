package com.devicemanagement.module.device.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * alarm
 */
@Data
public class Alarm implements Serializable {
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
    private Byte status;

    /**
     * 处理人
     */
    private String handler;

    private static final long serialVersionUID = 1L;
}