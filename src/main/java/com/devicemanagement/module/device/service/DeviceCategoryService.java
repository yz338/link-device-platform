package com.devicemanagement.module.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.devicemanagement.module.device.dto.DeviceCategoryDTO;
import com.devicemanagement.module.device.entity.DeviceCategory;

import java.util.List;

public interface DeviceCategoryService extends IService<DeviceCategory> {
    List<DeviceCategory> deviceCategoryList(DeviceCategoryDTO deviceCategoryDTO);
    DeviceCategory addDeviceCategory(DeviceCategoryDTO deviceCategoryDTO);
    DeviceCategory updateDeviceCategory(DeviceCategoryDTO deviceCategoryDTO);
    String  deleteDeviceCategory(Integer id);
}
