package com.devicemanagement.module.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.devicemanagement.module.device.entity.pulldata.DeviceSubmissionSurfaceWater;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;

public interface DeviceSubmissionSurfaceWaterMapper extends BaseMapper<DeviceSubmissionSurfaceWater> {

    @Select("SELECT MAX(data_time) FROM device_submission_surface_water WHERE mn = #{mn}")
    Date selectLatestSubmitTime(@Param("mn") Long mn);
}
