package com.devicemanagement.module.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.devicemanagement.module.device.entity.pulldata.DeviceSubmissionTvoc;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

public interface DeviceSubmissionTvocMapper extends BaseMapper<DeviceSubmissionTvoc> {

    @Select("SELECT MAX(data_time) FROM device_submission_tvoc WHERE mn = #{mn}")
    Date selectLatestSubmitTime(@Param("mn") String mn);
    @Select("SELECT * FROM device_submission_tvoc WHERE mn = #{mn} ORDER BY data_time DESC Limit 10")
    List<DeviceSubmissionTvoc> selectTop10ByMn(@Param("mn") String mn);
}
