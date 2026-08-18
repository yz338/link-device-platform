package com.devicemanagement.module.device.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;

/**
 * device_attr_air_station
 */
@TableName("device_attr_air_station")
@Data
public class DeviceAttrAirStation implements Serializable {
    /**
     * 属性ID
     */
    @Id
    private Long id;

    /**
     * 二氧化硫最小值
     */
    private BigDecimal so2Min;

    /**
     * 二氧化硫平均值
     */
    private BigDecimal so2Avg;

    /**
     * 二氧化硫最大值
     */
    private BigDecimal so2Max;

    /**
     * 一氧化氮最小值
     */
    private BigDecimal noMin;

    /**
     * 一氧化氮平均值
     */
    private BigDecimal noAvg;

    /**
     * 一氧化氮最大值
     */
    private BigDecimal noMax;

    /**
     * 二氧化氮最小值
     */
    private BigDecimal no2Min;

    /**
     * 二氧化氮平均值
     */
    private BigDecimal no2Avg;

    /**
     * 二氧化氮最大值
     */
    private BigDecimal no2Max;

    /**
     * 氮氧化物最小值
     */
    private BigDecimal noxMin;

    /**
     * 氮氧化物平均值
     */
    private BigDecimal noxAvg;

    /**
     * 氮氧化物最大值
     */
    private BigDecimal noxMax;

    /**
     * 一氧化碳最小值
     */
    private BigDecimal coMin;

    /**
     * 一氧化碳平均值
     */
    private BigDecimal coAvg;

    /**
     * 一氧化碳最大值
     */
    private BigDecimal coMax;

    /**
     * 可吸入颗粒物PM10最小值
     */
    private BigDecimal pm10Min;

    /**
     * 可吸入颗粒物PM10平均值
     */
    private BigDecimal pm10Avg;

    /**
     * 可吸入颗粒物PM10最大值
     */
    private BigDecimal pm10Max;

    /**
     * 细颗粒物PM2.5最小值
     */
    private BigDecimal pm25Min;

    /**
     * 细颗粒物PM2.5平均值
     */
    private BigDecimal pm25Avg;

    /**
     * 细颗粒物PM2.5最大值
     */
    private BigDecimal pm25Max;

    private static final long serialVersionUID = 1L;
}