package com.devicemanagement.module.device.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * device_attr
 */
@Data
@TableName(value = "device_attr")
public class DeviceAttr {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
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