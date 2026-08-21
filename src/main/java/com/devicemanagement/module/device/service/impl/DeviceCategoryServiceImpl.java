package com.devicemanagement.module.device.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.devicemanagement.module.device.dto.DeviceCategoryDTO;
import com.devicemanagement.module.device.entity.Device;
import com.devicemanagement.module.device.entity.DeviceCategory;
import com.devicemanagement.module.device.mapper.DeviceCategoryMapper;
import com.devicemanagement.module.device.mapper.DeviceMapper;
import com.devicemanagement.module.device.service.DeviceCategoryService;
import com.devicemanagement.module.system.entity.SysUser;
import com.devicemanagement.module.system.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class DeviceCategoryServiceImpl extends ServiceImpl<DeviceCategoryMapper, DeviceCategory> implements DeviceCategoryService {
    public final SysUserMapper sysUserMapper;
    public final DeviceCategoryMapper deviceCategoryMapper;
    private final DeviceMapper deviceMapper;
    @Override
    public List<DeviceCategory> deviceCategoryList(DeviceCategoryDTO deviceCategoryDTO) {
        LambdaQueryWrapper<DeviceCategory> queryWrapper = new LambdaQueryWrapper<>();
        if (deviceCategoryDTO == null){
            return this.list();
        }
        if (deviceCategoryDTO.getCategory_name() != null){
            queryWrapper.like(DeviceCategory::getCategoryName, deviceCategoryDTO.getCategory_name());
        }
        if(deviceCategoryDTO.getCreate_by()!=null){
            queryWrapper.like(DeviceCategory::getCreateBy, deviceCategoryDTO.getCreate_by());
        }
        if (deviceCategoryDTO.getStatus() != null){
            queryWrapper.eq(DeviceCategory::getStatus, deviceCategoryDTO.getStatus());
        }
        return this.list(queryWrapper);
    }

    @Override
    public DeviceCategory addDeviceCategory(DeviceCategoryDTO deviceCategoryDTO) {
        DeviceCategory deviceCategory = new DeviceCategory();
        deviceCategory.setCategoryName(deviceCategoryDTO.getCategory_name());
        deviceCategory.setDescription(deviceCategoryDTO.getDescription());

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getName() != null){
            LambdaQueryWrapper<SysUser> queryWrapper=new LambdaQueryWrapper();
            SysUser sysUser=sysUserMapper.selectOne(queryWrapper.eq(SysUser::getUsername,authentication.getName()));
            if(sysUser!=null){
                deviceCategory.setCreateBy(sysUser.getId());
            }
        }
        deviceCategory.setStatus((byte) 0);
        this.save(deviceCategory);
        return deviceCategory;
    }

    @Override
    public DeviceCategory updateDeviceCategory(DeviceCategoryDTO deviceCategoryDTO) {
        LambdaUpdateWrapper<DeviceCategory> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(DeviceCategory::getId, deviceCategoryDTO.getId());
        updateWrapper.set(DeviceCategory::getCategoryName, deviceCategoryDTO.getCategory_name());
        updateWrapper.set(DeviceCategory::getDescription, deviceCategoryDTO.getDescription());
        updateWrapper.set(DeviceCategory::getStatus, deviceCategoryDTO.getStatus());
        this.update(updateWrapper);
        return this.getById(deviceCategoryDTO.getId());
    }

    @Override
    public String deleteDeviceCategory(Integer id) {
        LambdaQueryWrapper<Device> wrapper=new LambdaQueryWrapper<>();
        List<Device> list=deviceMapper.selectList(wrapper.eq(Device::getDeviceCategoryId,id));
        if(!list.isEmpty()){
            return "该类别下有设备，不能删除";
        }
        deviceCategoryMapper.deleteById(id);
        return "删除成功";
    }
}
