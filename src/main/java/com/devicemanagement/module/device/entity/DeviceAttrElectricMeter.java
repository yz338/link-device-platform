package com.devicemanagement.module.device.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * device_attr_electric_meter
 */
@Data
@TableName("device_attr_electric_meter")
public class DeviceAttrElectricMeter implements Serializable {
    /**
     * 属性ID
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    /**
     * 电流互感器倍率
     */
    private BigDecimal ct_ratio;

    /**
     * 第一象限无功电能
     */
    private BigDecimal q1eq;

    /**
     * 第二象限无功电能
     */
    private BigDecimal q2eq;

    /**
     * 第三象限无功电能
     */
    private BigDecimal q3eq;

    /**
     * 第四象限无功电能
     */
    private BigDecimal q4eq;

    /**
     * 三相线电压Uab
     */
    private BigDecimal uab;

    /**
     * 三相线电压Ubc
     */
    private BigDecimal ubc;

    /**
     * 三相线电压Uca
     */
    private BigDecimal uca;

    /**
     * 三相相电压Ua
     */
    private BigDecimal ua;

    /**
     * 三相相电压Ub
     */
    private BigDecimal ub;

    /**
     * 三相相电压Uc
     */
    private BigDecimal uc;

    /**
     * 三相电流Ia
     */
    private BigDecimal ia;

    /**
     * 三相电流Ib
     */
    private BigDecimal ib;

    /**
     * 三相电流Ic
     */
    private BigDecimal ic;

    /**
     * 合相有功功率
     */
    private BigDecimal pt;

    /**
     * A相有功功率
     */
    private BigDecimal pa;

    /**
     * B相有功功率
     */
    private BigDecimal pb;

    /**
     * C相有功功率
     */
    private BigDecimal pc;

    /**
     * 合相无功功率
     */
    private BigDecimal qt;

    /**
     * A相无功功率
     */
    private BigDecimal qa;

    /**
     * B相无功功率
     */
    private BigDecimal qb;

    /**
     * C相无功功率
     */
    private BigDecimal qc;

    /**
     * 合相功率因数
     */
    private BigDecimal pft;

    /**
     * A相功率因数
     */
    private BigDecimal pfa;

    /**
     * B相功率因数
     */
    private BigDecimal pfb;

    /**
     * C相功率因数
     */
    private BigDecimal pfc;

    /**
     * 频率
     */
    private BigDecimal freq;

    private static final long serialVersionUID = 1L;
}