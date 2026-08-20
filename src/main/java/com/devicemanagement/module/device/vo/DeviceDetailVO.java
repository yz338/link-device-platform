package com.devicemanagement.module.device.vo;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class DeviceDetailVO {
    // 通用字段（所有类型都有）
    private Long id;
    private String deviceName;
    private String location;
    private String categoryName;    // 设备类别名
    private String attrTag;         // 设备类型标识：electric_meter/air_station/...
    private Long attrId;           // 设备类型ID

    // 动态属性：不同设备类型放不同内容
    // key = 属性名（中文显示名）, value = 属性值
    private Map<String, Object> attributes;

    // 最新报送时间
    private Date latestSubmitTime;
}