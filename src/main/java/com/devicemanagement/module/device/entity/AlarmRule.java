package com.devicemanagement.module.device.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * alarm_rule
 */
@Data
public class AlarmRule implements Serializable {
    /**
     * 规则ID
     */
    private Long id;

    /**
     * 告警设备
     */
    private String alarmDevice;

    /**
     * 关联告警指标ID
     */
    private Long alarmIndicatorId;

    /**
     * 告警通知频率
     */
    private Integer notifyInterval;

    /**
     * 顶层条件关联关系
     */
    private String topConditionRelation;

    /**
     * 规则关联关系
     */
    private String ruleRelation;

    /**
     * 关联设备类别ID
     */
    private Long deviceCategoryId;

    /**
     * 规则创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 规则创建人
     */
    private Long createBy;

    /**
     * 启用状态
     */
    private Byte status;

    /**
     * 逻辑删除
     */
    private Byte isDeleted;

    private static final long serialVersionUID = 1L;
}