package com.devicemanagement.module.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.devicemanagement.module.system.dto.DeviceDTO;
import com.devicemanagement.module.device.entity.Device;

import java.util.List;

public interface DeviceService extends IService<Device> {
    List<Device> deviceList(DeviceDTO deviceDTO);

    void save(DeviceDTO deviceDTO);
}
