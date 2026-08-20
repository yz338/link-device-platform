package com.devicemanagement.module.device.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.Date;

@Data
@Schema(description = "设备类别DTO")
public class DeviceCategoryDTO {
    /**
     * 设备类别ID
     */
    @Schema(description = "设备类别ID", example = "1")
    private Long id;

    /**
     * 设备类别
     */
    @Schema(description = "设备类别名称", example = "用电设备")
    private String category_name;

    /**
     * 类别描述
     */
    @Schema(description = "类别描述", example = "用电量信息检测")
    private String description;

    /**
     * 启用状态
     */
    @Schema(description = "启用状态：0=禁用，1=启用", example = "1")
    private Integer status;

    /**
     * 创建人
     */
    @Schema(description = "创建人ID")
    private Long create_by;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private Date create_time;

    @Schema(description = "更新时间")
    private Integer update_time;

    private static final long serialVersionUID = 1L;

    @Schema(description = "页码（从1开始）", example = "1")
    private Integer pageNum;

    @Schema(description = "每页条数", example = "10")
    private Integer pageSize;
}
