package com.devicemanagement.module.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.devicemanagement.module.device.entity.pulldata.DeviceSubmissionAirStation;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;

public interface DeviceSubmissionAirStationMapper extends BaseMapper<DeviceSubmissionAirStation> {

    @Select("SELECT MAX(data_time) FROM device_submission_air_station WHERE mn = #{mn}")
    Date selectLatestSubmitTime(@Param("mn") Long mn);
}
