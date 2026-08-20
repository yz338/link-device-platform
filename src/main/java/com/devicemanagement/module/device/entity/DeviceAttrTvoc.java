package com.devicemanagement.module.device.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * device_attr_tvoc
 */
@Data
@TableName("device_attr_tvoc")
public class DeviceAttrTvoc implements Serializable {
    /**
     * 属性ID
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    /**
     * 温度
     */
    private BigDecimal temperature;

    /**
     * 湿度
     */
    private BigDecimal humidity;

    /**
     * 气压
     */
    private BigDecimal air_pressure;

    /**
     * 风速
     */
    private BigDecimal wind_speed;

    /**
     * 风向
     */
    private String wind_direction;

    private static final long serialVersionUID = 1L;

}