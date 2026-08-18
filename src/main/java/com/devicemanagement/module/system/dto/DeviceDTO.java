package com.devicemanagement.module.system.dto;

import lombok.Data;

@Data
public class DeviceDTO {
    private Integer pageNum;
    private Integer pageSize;
    private Long deviceCategoryId;
    private String deviceName;
    private Long id;
    private String location;
    private Long status;
}
