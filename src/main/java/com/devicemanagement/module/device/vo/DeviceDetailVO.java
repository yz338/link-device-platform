package com.devicemanagement.module.device.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Schema(description = "设备详情VO")
public class DeviceDetailVO {
    // 通用字段（所有类型都有）
    @Schema(description = "设备ID")
    private Long id;

    @Schema(description = "设备名称")
    private String deviceName;

    @Schema(description = "设备位置")
    private String location;

    @Schema(description = "设备类别名称")
    private String categoryName;

    @Schema(description = "设备类型标识：electric_meter/air_station/surface_water/tvoc")
    private String attrTag;

    @Schema(description = "对应属性表的主键ID")
    private Long attrId;

    // 动态属性：不同设备类型放不同内容
    @Schema(description = "动态属性集合，不同设备类型包含不同属性")
    private Map<String, Object> attributes;

    // 最新报送时间
    @Schema(description = "最新报送时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date latestSubmitTime;

    // 设备推送数据
    @Schema(description = "设备推送数据（最新前10条）")
    private List<?> data;
}
