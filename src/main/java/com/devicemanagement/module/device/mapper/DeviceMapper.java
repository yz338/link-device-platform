package com.devicemanagement.module.device.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.devicemanagement.module.device.entity.Device;
import com.devicemanagement.module.device.vo.DeviceDetailVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DeviceMapper extends BaseMapper<Device> {
    DeviceDetailVO selectDetailById(@Param("id") Long id);

}