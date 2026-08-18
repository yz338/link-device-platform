package com.devicemanagement.module.device.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.devicemanagement.module.device.dto.DeviceDTO;
import com.devicemanagement.module.device.entity.Device;
import com.devicemanagement.module.device.mapper.DeviceMapper;
import com.devicemanagement.module.device.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements DeviceService{

    @Override
    public List<Device> deviceList(DeviceDTO deviceDTO) {
        LambdaQueryWrapper<Device> queryWrapper = new LambdaQueryWrapper<>();
        if (deviceDTO==null){
            return this.list();
        }
        if (deviceDTO.getDeviceName() != null){
            queryWrapper.like(Device::getDeviceName, deviceDTO.getDeviceName());
        }
        if (deviceDTO.getId() != null){
            queryWrapper.eq(Device::getId, deviceDTO.getId());
        }
        if (deviceDTO.getLocation() != null){
            queryWrapper.like(Device::getLocation, deviceDTO.getLocation());
        }
        if (deviceDTO.getStatus()!=null){
            queryWrapper.eq(Device::getStatus, deviceDTO.getStatus());
        }
        return this.list(queryWrapper);
    }

    @Override
    public void save(DeviceDTO deviceDTO) {
        Device device = new Device();
        BeanUtils.copyProperties(deviceDTO, device);
        this.save(device);
    }
}
