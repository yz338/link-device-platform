package com.devicemanagement.module.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.devicemanagement.module.device.entity.DeviceAttrTvoc;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;


public interface DeviceAttrTvocMapper extends BaseMapper<DeviceAttrTvoc> {
    @Insert("INSERT INTO device_attr_tvoc (id) VALUES (null)")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insertEmpty(DeviceAttrTvoc entity);
}