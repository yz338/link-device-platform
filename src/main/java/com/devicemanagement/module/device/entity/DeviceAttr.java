package com.devicemanagement.module.device.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * device_attr
 */
@Data
public class DeviceAttr implements Serializable {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 属性表标识：surface_water/tvoc/air_station/electric_meter
     */
    private String attrTag;

    /**
     * 对应属性表的主键ID
     */
    private Long attrId;

    /**
     * 设备ID
     */
    private Long deviceId;

    private static final long serialVersionUID = 1L;
}