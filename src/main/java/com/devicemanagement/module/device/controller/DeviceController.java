package com.devicemanagement.module.device.controller;

import com.devicemanagement.common.Result;
import com.devicemanagement.module.system.dto.DeviceDTO;
import com.devicemanagement.module.device.entity.Device;
import com.devicemanagement.module.device.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}
