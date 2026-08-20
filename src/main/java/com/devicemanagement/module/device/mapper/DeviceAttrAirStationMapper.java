package com.devicemanagement.module.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.devicemanagement.module.device.entity.DeviceAttrAirStation;
import com.devicemanagement.module.device.entity.DeviceAttrElectricMeter;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;

public interface DeviceAttrAirStationMapper extends BaseMapper<DeviceAttrAirStation> {
    @Insert("INSERT INTO device_attr_air_station () VALUES ()")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insertEmpty(DeviceAttrAirStation entity);
}