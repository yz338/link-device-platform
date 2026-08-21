package com.devicemanagement.module.alarm.bean;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * alarm_indicator
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "告警指标对象")
public class AlarmIndicator implements Serializable {
    /**
     * 告警指标ID
     */
    @Schema(description = "告警指标ID", example = "1")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 告警指标
     */
    @Schema(description = "告警指标", example = "温度")
    private String indicatorName;

    /**
     * 指标描述
     */
    @Schema(description = "指标描述", example = "温度过高")
    private String description;

    /**
     * 指标启用状态
     */
    @Schema(description = "指标启用状态：1=启用 2=禁用", example = "1")
    @TableField(value = "status", fill =FieldFill.DEFAULT)
    private Byte status;

    /**
     * 指标创建时间
     */
    @Schema(description = "指标创建时间", example = "2021-04-15 12:00:00")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;
    /**
     * 指标更新时间
     */
    @Schema(description = "指标更新时间", example = "2021-04-15 12:00:00")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    /**
     * 关联设备Id
     */
    @Schema(description = "关联设备Id", example = "1")
    private Long deviceId;
    /**
     * 关联设备类别Id
     */
    @Schema(description = "关联设备类别Id", example = "1")
    private Long deviceCategoryId;
    private static final long serialVersionUID = 1L;
}