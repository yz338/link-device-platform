package com.devicemanagement.module.device.entity;

import java.io.Serializable;
import java.util.Date;

import cn.afterturn.easypoi.excel.annotation.Excel;
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
    @Excel(name = "设备名称")
    private String deviceName;

    /**
     * 设备类别ID
     */
    @Excel(name = "设备类别ID")
    private Long deviceCategoryId;

    /**
    * 设备位置
    */
    @Excel(name = "设备位置")
    private String location;
    /**
     * 设备属性
     * 0离线 1在线
     */
    @Excel(name = "设备状态",replace = {"在线_1","离线_0"})
    private Long status;
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @Excel(name = "创建时间", format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @Excel(name = "更新时间", format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;


    private static final long serialVersionUID = 1L;
}