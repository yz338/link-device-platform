package com.devicemanagement.module.device.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * device_info
 */
@Data
@TableName(value = "device_info")
public class Device implements Serializable {
    /**
     * 设备ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 设备名称
     */
    @TableField(value = "device_name")
    private String deviceName;

    /**
     * 设备类别ID
     */
    @TableField(value = "device_category_id")
    private Long deviceCategoryId;

    /**
     * 设备属性ID
     */
    @TableField(value = "device_attr_id")
    private Long deviceAttrId;

    private String location;

    private Long status;
    @TableField(value = "creat_time", fill = FieldFill.INSERT)
    private Date creatTime;
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


    private static final long serialVersionUID = 1L;
}