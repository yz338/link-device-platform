package com.devicemanagement.module.device.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * device_category
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "device_category")
public class DeviceCategory {
    /**
     * 设备类别ID
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    /**
     * 设备类别
     */
    @NotBlank
    private String categoryName;

    /**
     * 类别描述
     */
    @NotBlank
    private String description;

    /**
     * 启用状态
     */
    private Byte status;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.UPDATE)
    private Integer updateTime;

}