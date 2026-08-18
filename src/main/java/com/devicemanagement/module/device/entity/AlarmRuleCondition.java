package com.devicemanagement.module.device.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * alarm_rule_condition
 */
@Data
public class AlarmRuleCondition implements Serializable {
    /**
     * 条件ID
     */
    private Long id;

    /**
     * 规则ID
     */
    private Long ruleId;

    /**
     * 父条件ID
     */
    private Long parentId;

    /**
     * 告警属性
     */
    private String alarmAttribute;

    /**
     * 比较条件
     */
    private String operator;

    /**
     * 告警阈值
     */
    private BigDecimal threshold;

    /**
     * 条件排序
     */
    private Integer sort;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}