package com.devicemanagement.module.device.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * device_category
 */
@Data
public class DeviceCategory implements Serializable {
    /**
     * 设备类别ID
     */
    private Long id;

    /**
     * 设备类别
     */
    private String category_name;

    /**
     * 类别描述
     */
    private String description;

    /**
     * 启用状态
     */
    private Byte status;

    /**
     * 创建人
     */
    private Long create_by;

    /**
     * 创建时间
     */
    private Date create_time;

    private Integer update_time;

    private static final long serialVersionUID = 1L;
}