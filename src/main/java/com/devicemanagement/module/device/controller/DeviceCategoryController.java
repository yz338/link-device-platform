package com.devicemanagement.module.device.controller;

import com.devicemanagement.common.Result;
import com.devicemanagement.module.device.dto.DeviceCategoryDTO;
import com.devicemanagement.module.device.entity.DeviceCategory;
import com.devicemanagement.module.device.service.DeviceCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/deviceCategory")
public class DeviceCategoryController {
    @Autowired
    private DeviceCategoryService deviceCategoryService;
    @PostMapping("/query")
    public Result<List<DeviceCategory>> deviceCategoryList(@RequestBody DeviceCategoryDTO deviceCategoryDTO){
        return Result.success(deviceCategoryService.deviceCategoryList(deviceCategoryDTO));
    }
    @GetMapping("/queryById")
    public Result<DeviceCategory> deviceCategoryById(@RequestParam Integer id){
        return Result.success(deviceCategoryService.getById(id));
    }
    @PostMapping("/addDeviceCategory")
    public Result<DeviceCategory> addDeviceCategory(@RequestBody DeviceCategoryDTO deviceCategoryDTO){
        return Result.success(deviceCategoryService.addDeviceCategory(deviceCategoryDTO));
    }
    @PutMapping("/updateDeviceCategory")
    public Result<DeviceCategory> updateDeviceCategory(@RequestBody DeviceCategoryDTO deviceCategoryDTO){
        return Result.success(deviceCategoryService.updateDeviceCategory(deviceCategoryDTO));
    }
    @DeleteMapping("/deleteDeviceCategory")
    public Result<String> deleteDeviceCategory(@RequestParam Integer id){
        String category = deviceCategoryService.deleteDeviceCategory(id);
        return Result.success(category);
    }
}
