package com.devicemanagement.module.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.devicemanagement.module.device.entity.DeviceAttrSurfaceWater;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;


public interface DeviceAttrSurfaceWaterMapper extends BaseMapper<DeviceAttrSurfaceWater> {
    @Insert("INSERT INTO device_attr_surface_water (id) VALUES (null)")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insertEmpty(DeviceAttrSurfaceWater entity);
}