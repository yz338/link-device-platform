package com.devicemanagement.module.device.controller;

import com.devicemanagement.common.Result;
import com.devicemanagement.module.device.dto.DeviceCategoryDTO;
import com.devicemanagement.module.device.entity.DeviceCategory;
import com.devicemanagement.module.device.service.DeviceCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/deviceCategory")
@Tag(name = "设备类别管理", description = "设备类别的增删改查接口")
public class DeviceCategoryController {
    @Autowired
    private DeviceCategoryService deviceCategoryService;

    @PostMapping("/query")
    @Operation(summary = "查询设备类别列表", description = "支持按条件筛选设备类别")
    public Result<List<DeviceCategory>> deviceCategoryList(@RequestBody DeviceCategoryDTO deviceCategoryDTO) {
        return Result.success(deviceCategoryService.deviceCategoryList(deviceCategoryDTO));
    }

    @GetMapping("/queryById")
    @Operation(summary = "根据ID查询设备类别", description = "根据设备类别ID查询详情")
    public Result<DeviceCategory> deviceCategoryById(
            @Parameter(description = "设备类别ID", required = true) @RequestParam Integer id) {
        return Result.success(deviceCategoryService.getById(id));
    }

    @PostMapping("/addDeviceCategory")
    @Operation(summary = "新增设备类别", description = "新增设备类别，category_name和description必填")
    public Result<DeviceCategory> addDeviceCategory(@RequestBody DeviceCategoryDTO deviceCategoryDTO) {
        return Result.success(deviceCategoryService.addDeviceCategory(deviceCategoryDTO));
    }

    @PutMapping("/updateDeviceCategory")
    @Operation(summary = "修改设备类别", description = "根据ID修改设备类别信息")
    public Result<DeviceCategory> updateDeviceCategory(@RequestBody DeviceCategoryDTO deviceCategoryDTO) {
        return Result.success(deviceCategoryService.updateDeviceCategory(deviceCategoryDTO));
    }

    @DeleteMapping("/deleteDeviceCategory")
    @Operation(summary = "删除设备类别", description = "根据ID删除设备类别")
    public Result<String> deleteDeviceCategory(
            @Parameter(description = "设备类别ID", required = true) @RequestParam Integer id) {
        String category = deviceCategoryService.deleteDeviceCategory(id);
        return Result.success(category);
    }
}
