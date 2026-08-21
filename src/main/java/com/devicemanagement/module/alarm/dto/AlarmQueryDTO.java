package com.devicemanagement.module.alarm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "告警查询DTO")
public class AlarmQueryDTO {
    @Schema(description = "设备名称（模糊）")
    private String deviceName;

    @Schema(description = "设备类别")
    private String deviceCategory;

    @Schema(description = "状态：1=未处理 2=处理中 3=已处理")
    private Integer status;

    @Schema(description = "页码", example = "1")
    private Integer pageNum = 1;

    @Schema(description = "每页条数", example = "10")
    private Integer pageSize = 10;
}
