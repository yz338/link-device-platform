package com.devicemanagement.module.device.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "设备属性更新DTO")
public class DeviceAttrUpdateDTO {
    @Schema(description = "设备ID", example = "8")
    private Long deviceId;

    @Schema(description = "要更新的属性集合，key=字段名，value=新值。未传的字段保持原值", example = "{\"temperature\": 26, \"humidity\": 55}")
    private Map<String, Object> attributes;
}
