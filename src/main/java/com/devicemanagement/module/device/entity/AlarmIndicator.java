package com.devicemanagement.module.device.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * alarm_indicator
 */
@Data
public class AlarmIndicator implements Serializable {
    /**
     * 告警指标ID
     */
    private Long id;

    /**
     * 告警指标
     */
    private String indicatorName;

    /**
     * 指标描述
     */
    private String description;

    /**
     * 指标启用状态
     */
    private Byte status;

    /**
     * 指标创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}