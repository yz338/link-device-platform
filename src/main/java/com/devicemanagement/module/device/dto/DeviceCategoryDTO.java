package com.devicemanagement.module.device.dto;

import lombok.Data;
import java.util.Date;

@Data
public class DeviceCategoryDTO {
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
    private Integer  status;

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
    private Integer pageNum;
    private Integer pageSize;
}
