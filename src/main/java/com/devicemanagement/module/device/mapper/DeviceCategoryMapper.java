package com.devicemanagement.module.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.devicemanagement.module.device.entity.DeviceCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.mybatis.spring.annotation.MapperScan;

import java.util.List;

@Mapper
public interface DeviceCategoryMapper extends BaseMapper<DeviceCategory> {
    @Select("select * from device_category where category_name = #{categoryName}")
    public DeviceCategory selectByName(String categoryName);
}