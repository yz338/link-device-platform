package com.devicemanagement.module.device.entity;

import java.io.Serializable;
import java.util.Date;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * device_info
 */
@Data
@TableName(value = "device_info")
@Schema(description = "设备信息对象")
public class Device implements Serializable {
    /**
     * 设备ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "设备ID", example = "1")
    private Long id;

    /**
     * 设备名称
     */
    @Excel(name = "设备名称")
    @Schema(description = "设备名称", example = "温湿度传感器A")
    private String deviceName;

    /**
     * 设备类别ID
     */
    @Excel(name = "设备类别ID")
    @Schema(description = "设备类别ID", example = "1")
    private Long deviceCategoryId;

    /**
    * 设备位置
    */
    @Excel(name = "设备位置")
    @Schema(description = "设备位置", example = "一号车间")
    private String location;

    /**
    * 设备属性
    * 0离线 1在线
    */
    @Excel(name = "设备状态",replace = {"在线_1","离线_0"})
    @Schema(description = "设备状态：0=离线，1=在线", example = "1")
    private Long status;
    /**
    * 创建时间
    */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @Excel(name = "创建时间", format = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private Date createTime;
    /**
    * 更新时间
    */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @Excel(name = "更新时间", format = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间")
    private Date updateTime;


    private static final long serialVersionUID = 1L;
}
