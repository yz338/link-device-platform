package com.devicemanagement.module.device.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "设备DTO")
public class DeviceDTO {
    @Schema(description = "页码（从1开始）", example = "1")
    private Integer pageNum;

    @Schema(description = "每页条数", example = "10")
    private Integer pageSize;

    @Schema(description = "设备类别ID", example = "1")
    private Long deviceCategoryId;

    @Schema(description = "设备类别名称", example = "用电设备")
    private String deviceCategoryName;

    @Schema(description = "设备名称", example = "温湿度传感器A")
    private String deviceName;

    @Schema(description = "设备ID", example = "1")
    private Long id;

    @Schema(description = "设备位置", example = "一号车间")
    private String location;

    @Schema(description = "设备状态：0=离线，1=在线", example = "1")
    private Long status;
}
