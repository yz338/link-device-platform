package com.devicemanagement.module.device.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "设备类别对象")
public class DeviceCategory {
    /**
     * 设备类别ID
     */
    @TableId(value = "id",type = IdType.AUTO)
    @Schema(description = "设备类别ID")
    private Long id;

    /**
     * 设备类别
     */
    @NotBlank
    @Schema(description = "设备类别名称", example = "用电设备")
    private String categoryName;

    /**
     * 类别描述
     */
    @NotBlank
    @Schema(description = "类别描述", example = "用电量信息检测")
    private String description;

    /**
     * 启用状态
     */
    @Schema(description = "启用状态：0=禁用，1=启用", example = "1")
    private Byte status;

    /**
     * 创建人
     */
    @Schema(description = "创建人ID")
    private Long createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private Date createTime;
    @TableField(fill = FieldFill.UPDATE)
    @Schema(description = "更新时间")
    private Integer updateTime;

}
