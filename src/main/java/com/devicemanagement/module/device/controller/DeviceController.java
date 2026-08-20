package com.devicemanagement.module.device.controller;

import com.devicemanagement.common.Result;
import com.devicemanagement.module.device.dto.DeviceDTO;
import com.devicemanagement.module.device.entity.Device;
import com.devicemanagement.module.device.service.DeviceService;
import com.devicemanagement.module.device.vo.DeviceDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/device")
public class DeviceController {
    @Autowired
    private DeviceService deviceService;

    @GetMapping("/list")
    public Result<List<Device>> deviceList(DeviceDTO deviceDTO){
        return Result.success(deviceService.deviceList(deviceDTO));
    }

    @PostMapping("/addDevice")
    public Result<String> addDevice(@RequestBody DeviceDTO deviceDTO){
        deviceService.save(deviceDTO);
        return Result.success("添加成功");
    }
    @PutMapping("/updateDevice")
    public Result<Device> updateDevice(@RequestBody DeviceDTO deviceDTO){
        Device device = deviceService.updateDevice(deviceDTO);
        return Result.success(device);
    }
    @DeleteMapping("/deleteDevice")
    public Result<String> deleteDevice(@RequestParam Long id){
        String result = deviceService.deleteDeviceById(id);
        return Result.success(result);
    }
    @GetMapping("/queryCategoryName")
    public Result<List<String>> queryCategoryName(){
        List<String> CategoryName = deviceService.queryCategoryName(null);
        return Result.success(CategoryName);
    }
    @GetMapping("/queryCategoryIdByCategoryName")
    public Result<List<Device>> queryByCategory(@RequestParam String categoryName){
        List<Device> devices = deviceService.queryByCategory(categoryName);
        return Result.success(devices);
    }
    @PostMapping("/importExcel")
    public Result<String> importExcel(@RequestParam("file")  MultipartFile file) throws Exception {
        deviceService.importExcel(file);
        return Result.success("导入成功");
    }
    @GetMapping("/Detail")
    public Result<DeviceDetailVO> deviceDetail(@RequestParam Long id){
        DeviceDetailVO deviceDetail = deviceService.getDeviceDetail(id);
        return Result.success(deviceDetail);
    }
}
