package com.devicemanagement.module.device.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

/**
 * device_attr_surface_water
 */
@Data
public class DeviceAttrSurfaceWater implements Serializable {
    /**
     * 属性ID
     */
    private Long id;

    /**
     * 水温
     */
    private BigDecimal water_temperature;

    /**
     * pH值
     */
    private BigDecimal ph_value;

    /**
     * 溶解氧
     */
    private BigDecimal dissolved_oxygen;

    /**
     * 电导率
     */
    private BigDecimal conductivity;

    /**
     * 高锰酸盐指数
     */
    private BigDecimal permanganate_index;

    /**
     * 氨氮
     */
    private BigDecimal ammonia_nitrogen;

    /**
     * 总磷
     */
    private BigDecimal total_phosphorus;

    /**
     * 总氮
     */
    private BigDecimal total_nitrogen;

    private static final long serialVersionUID = 1L;
}