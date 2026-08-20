package com.devicemanagement.module.device.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * device_attr
 */
@Data
@TableName(value = "device_attr")
@Schema(description = "设备属性映射对象")
public class DeviceAttr {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;

    /**
     * 属性表标识：surface_water/tvoc/air_station/electric_meter
     */
    @Schema(description = "属性表标识：electric_meter/air_station/surface_water/tvoc", example = "electric_meter")
    private String attrTag;

    /**
     * 对应属性表的主键ID
     */
    @Schema(description = "对应属性表的主键ID")
    private Long attrId;

    /**
     * 设备ID
     */
    @Schema(description = "设备ID")
    private Long deviceId;

    private static final long serialVersionUID = 1L;
}
