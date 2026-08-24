package com.devicemanagement.module.device.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

@Data
public class DeviceTemplateVO {

    @Excel(name = "设备名称", width = 25)
    private String deviceName;

    @Excel(name = "设备类别", width = 18)
    private String deviceCategoryId;

    @Excel(name = "设备位置", width = 25)
    private String location;

    @Excel(name = "设备状态", replace = {"在线_1", "离线_0"}, width = 12)
    private Long status;
}