package com.devicemanagement.module.device.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.devicemanagement.module.device.dto.DeviceCategoryDTO;
import com.devicemanagement.module.device.dto.DeviceDTO;
import com.devicemanagement.module.device.entity.*;
import com.devicemanagement.module.device.mapper.*;
import com.devicemanagement.module.device.service.DeviceCategoryService;
import com.devicemanagement.module.device.service.DeviceService;
import com.devicemanagement.module.device.vo.DeviceDetailVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements DeviceService{
    private final DeviceCategoryService deviceCategoryService;
    private final DeviceCategoryMapper deviceCategoryMapper;
    private final DeviceMapper deviceMapper;

    private final DeviceAttrElectricMeterMapper electricMeterMapper;

    private final DeviceAttrAirStationMapper airStationMapper;

    private final DeviceAttrSurfaceWaterMapper surfaceWaterMapper;

    private final DeviceAttrTvocMapper tvocMapper;

    private final DeviceSubmissionElectricityMapper electricityMapper;

    private final DeviceSubmissionAirStationMapper airStationSubmissionMapper;

    private final DeviceSubmissionSurfaceWaterMapper surfaceWaterSubmissionMapper;

    private final DeviceSubmissionTvocMapper tvocSubmissionMapper;

    private final DeviceAttrMapper deviceAttrMapper;
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
        if (deviceDTO.getDeviceCategoryId() != null) {
            queryWrapper.eq(Device::getDeviceCategoryId, deviceDTO.getDeviceCategoryId());
        }
        return this.list(queryWrapper);
    }

    @Override
    public void save(DeviceDTO deviceDTO) {
        Device device = new Device();
        BeanUtils.copyProperties(deviceDTO, device);
        this.save(device);
        createDeviceAttrForDevice(device);
    }

    @Override
    public Device updateDevice(DeviceDTO deviceDTO) {
        LambdaUpdateWrapper<Device> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Device::getId, deviceDTO.getId());
        LambdaQueryWrapper<DeviceCategory> queryWrapper = new LambdaQueryWrapper<>();
        DeviceCategory deviceCategory = deviceCategoryService.getOne(queryWrapper.eq(DeviceCategory::getCategoryName, deviceDTO.getDeviceCategoryName()));
        updateWrapper.set(Device::getDeviceCategoryId, deviceCategory.getId());
        updateWrapper.set(Device::getDeviceName, deviceDTO.getDeviceName());
        updateWrapper.set(Device::getLocation, deviceDTO.getLocation());
        this.update(updateWrapper);
        return this.getById(deviceDTO.getId());
    }

    @Override
    public String deleteDeviceById(Long id) {
        deviceMapper.deleteById(id);
        return "删除成功";
    }

    @Override
    public List<String> queryCategoryName(DeviceCategoryDTO deviceCategoryDTO) {
        List<DeviceCategory> deviceCategories = deviceCategoryService.deviceCategoryList(deviceCategoryDTO);
        List<String>  deviceCategoryNames = new ArrayList<>();
        for (DeviceCategory deviceCategory :deviceCategories) {
            deviceCategoryNames.add(deviceCategory.getCategoryName());
        }
        return deviceCategoryNames;
    }

    @Override
    public List<Device> queryByCategory(String categoryName) {
        DeviceCategory deviceCategory = deviceCategoryMapper.selectByName(categoryName);
        if (deviceCategory == null) {
            return List.of();
        }
        DeviceDTO deviceDTO=new DeviceDTO();
        deviceDTO.setDeviceCategoryId(deviceCategory.getId());
        return deviceList(deviceDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String importExcel(MultipartFile file) throws Exception {
        if (file.isEmpty()){
            return "文件为空,请选择文件";
        }
        ImportParams params=new ImportParams();
        params.setHeadRows(1);
        List<Device> devices = ExcelImportUtil.importExcel(file.getInputStream(), Device.class, params);
        if (devices == null || devices.isEmpty()) {
            return "文件内容为空";
        }
        for (Device device : devices) {
            if (device.getDeviceName() == null || device.getDeviceName().isEmpty()) {
                continue;
            }
            this.save(device);
            // 创建 device_attr 关联记录
            createDeviceAttrForDevice(device);
        }
        return "导入成功";
    }

    @Override
    public DeviceDetailVO getDeviceDetail(Long id) {
        // 1. 查基础信息
        DeviceDetailVO vo = deviceMapper.selectDetailById(id);
        if (vo == null) {
            return null;
        }

        // 2. 根据 attrTag 查具体属性表，组装 attributes
        Map<String, Object> attributes = new LinkedHashMap<>();
        if (vo.getAttrTag() != null && vo.getAttrId() != null) {
            switch (vo.getAttrTag()) {
                case "electric_meter":
                    loadElectricMeterAttr(vo.getAttrId(), attributes);
                    break;
                case "air_station":
                    loadAirStationAttr(vo.getAttrId(), attributes);
                    break;
                case "surface_water":
                    loadSurfaceWaterAttr(vo.getAttrId(), attributes);
                    break;
                case "tvoc":
                    loadTvocAttr(vo.getAttrId(), attributes);
                    break;
            }
        }
        vo.setAttributes(attributes);

        // 3. 查最新报送时间
        vo.setLatestSubmitTime(getLatestSubmitTime(vo.getAttrTag(), id));

        return vo;
    }
    private void loadElectricMeterAttr(Long attrId, Map<String, Object> attrs) {
        DeviceAttrElectricMeter attr = electricMeterMapper.selectById(attrId);
        if (attr != null) {
            attrs.put("电流互感器倍率", attr.getCt_ratio());
            attrs.put("第一象限无功电能 Q1Eq", attr.getQ1eq());
            attrs.put("第二象限无功电能 Q2Eq", attr.getQ2eq());
            attrs.put("第三象限无功电能 Q3Eq", attr.getQ3eq());
            attrs.put("第四象限无功电能 Q4Eq", attr.getQ4eq());
            attrs.put("三相线电压 Uab", attr.getUab());
            attrs.put("三相线电压 Ubc", attr.getUbc());
            attrs.put("三相线电压 Uca", attr.getUca());
            attrs.put("三相相电压 Ua", attr.getUa());
            attrs.put("三相相电压 Ub", attr.getUb());
            attrs.put("三相相电压 Uc", attr.getUc());
            attrs.put("三相电流 Ia", attr.getIa());
            attrs.put("三相电流 Ib", attr.getIb());
            attrs.put("三相电流 Ic", attr.getIc());
            attrs.put("合相有功功率 Pt", attr.getPt());
            attrs.put("A相有功功率 Pa", attr.getPa());
            attrs.put("B相有功功率 Pb", attr.getPb());
            attrs.put("C相有功功率 Pc", attr.getPc());
            attrs.put("合相无功功率 Qt", attr.getQt());
            attrs.put("A相无功功率 Qa", attr.getQa());
            attrs.put("B相无功功率 Qb", attr.getQb());
            attrs.put("C相无功功率 Qc", attr.getQc());
            attrs.put("合相功率因数 PFt", attr.getPft());
            attrs.put("A相功率因数 PFa", attr.getPfa());
            attrs.put("B相功率因数 PFb", attr.getPfb());
            attrs.put("C相功率因数 PFc", attr.getPfc());
            attrs.put("频率 Freq", attr.getFreq());
        }
    }

    private void loadAirStationAttr(Long attrId, Map<String, Object> attrs) {
        DeviceAttrAirStation attr = airStationMapper.selectById(attrId);
        if (attr != null) {
            attrs.put("SO2最小值", attr.getSo2Min());
            attrs.put("SO2平均值", attr.getSo2Avg());
            attrs.put("SO2最大值", attr.getSo2Max());
            attrs.put("NO最小值", attr.getNoMin());
            attrs.put("NO平均值", attr.getNoAvg());
            attrs.put("NO最大值", attr.getNoMax());
            attrs.put("NO2最小值", attr.getNo2Min());
            attrs.put("NO2平均值", attr.getNo2Avg());
            attrs.put("NO2最大值", attr.getNo2Max());
            attrs.put("NOx最小值", attr.getNoxMin());
            attrs.put("NOx平均值", attr.getNoxAvg());
            attrs.put("NOx最大值", attr.getNoxMax());
            attrs.put("CO最小值", attr.getCoMin());
            attrs.put("CO平均值", attr.getCoAvg());
            attrs.put("CO最大值", attr.getCoMax());
            attrs.put("PM10最小值", attr.getPm10Min());
            attrs.put("PM10平均值", attr.getPm10Avg());
            attrs.put("PM10最大值", attr.getPm10Max());
            attrs.put("PM2.5最小值", attr.getPm25Min());
            attrs.put("PM2.5平均值", attr.getPm25Avg());
            attrs.put("PM2.5最大值", attr.getPm25Max());
        }
    }

    private void loadSurfaceWaterAttr(Long attrId, Map<String, Object> attrs) {
        DeviceAttrSurfaceWater attr = surfaceWaterMapper.selectById(attrId);
        if (attr != null) {
            attrs.put("水温", attr.getWater_temperature());
            attrs.put("pH值", attr.getPh_value());
            attrs.put("溶解氧", attr.getDissolved_oxygen());
            attrs.put("电导率", attr.getConductivity());
            attrs.put("高锰酸盐指数", attr.getPermanganate_index());
            attrs.put("氨氮", attr.getAmmonia_nitrogen());
            attrs.put("总氮", attr.getTotal_nitrogen());
            attrs.put("总磷", attr.getTotal_phosphorus());
        }
    }

    private void loadTvocAttr(Long attrId, Map<String, Object> attrs) {
        DeviceAttrTvoc attr = tvocMapper.selectById(attrId);
        if (attr != null) {
            attrs.put("温度", attr.getTemperature());
            attrs.put("湿度", attr.getHumidity());
            attrs.put("风速", attr.getWind_speed());
            attrs.put("风向", attr.getWind_direction());
            attrs.put("气压", attr.getAir_pressure());
        }
    }

    private Date getLatestSubmitTime(String attrTag, Long deviceId) {
        // 1. 先通过 device_attr 映射表拿到 attr_id
        DeviceAttr deviceAttr = deviceAttrMapper.selectOne(
                new LambdaQueryWrapper<DeviceAttr>()
                        .eq(DeviceAttr::getDeviceId, deviceId)
                        .eq(DeviceAttr::getAttrTag, attrTag));
        if (deviceAttr == null) {
            return null;
        }

        // 2. 用 attr_id 作为 mn 值，查报送表的最新时间
        Long mn = deviceAttr.getAttrId();
        switch (attrTag) {
            case "electric_meter":
                return electricityMapper.selectLatestSubmitTime(mn);
            case "air_station":
                return airStationSubmissionMapper.selectLatestSubmitTime(mn);
            case "surface_water":
                return surfaceWaterSubmissionMapper.selectLatestSubmitTime(mn);
            case "tvoc":
                return tvocSubmissionMapper.selectLatestSubmitTime(mn);
            default:
                return null;
        }
    }
    /**
     * 根据类别名判断 attrTag
     */
    private String determineAttrTag(String categoryName) {
        if (categoryName == null) return null;
        // 根据你数据库里实际的类别名调整关键词
        if (categoryName.contains("电")) return "electric_meter";
        if (categoryName.contains("空气") || categoryName.contains("大气") || categoryName.contains("微站")) return "air_station";
        if (categoryName.contains("水")) return "surface_water";
        if (categoryName.contains("TVOC") || categoryName.contains("tvoc") || categoryName.contains("挥发性")) return "tvoc";
        return null;
    }

    /**
     * 在对应属性表创建空记录，返回主键 ID
     */
    private Long createEmptyAttrRecord(String attrTag) {
        switch (attrTag) {
            case "electric_meter":
                DeviceAttrElectricMeter electricMeter = new DeviceAttrElectricMeter();
                electricMeterMapper.insertEmpty(electricMeter);
                return electricMeter.getId();
            case "air_station":
                DeviceAttrAirStation airStation = new DeviceAttrAirStation();
                airStationMapper.insertEmpty(airStation);
                return airStation.getId();
            case "surface_water":
                DeviceAttrSurfaceWater surfaceWater = new DeviceAttrSurfaceWater();
                surfaceWaterMapper.insertEmpty(surfaceWater);
                return surfaceWater.getId();
            case "tvoc":
                DeviceAttrTvoc tvoc = new DeviceAttrTvoc();
                tvocMapper.insertEmpty(tvoc);
                return tvoc.getId();
            default:
                return null;
        }
    }
    /**
     * 根据设备的类别，自动创建 device_attr 关联记录
     */
    private void createDeviceAttrForDevice(Device device) {
        if (device.getDeviceCategoryId() == null) {
            return;
        }

        // 1. 查设备类别，确定 attrTag
        DeviceCategory category = deviceCategoryMapper.selectById(device.getDeviceCategoryId());
        if (category == null) {
            return;
        }
        String attrTag = determineAttrTag(category.getCategoryName());
        if (attrTag == null) {
            return;
        }

        // 2. 在对应属性表创建空记录
        Long attrId = createEmptyAttrRecord(attrTag);
        if (attrId == null) {
            return;
        }

        // 3. 创建 device_attr 关联记录
        DeviceAttr deviceAttr = new DeviceAttr();
        deviceAttr.setDeviceId(device.getId());
        deviceAttr.setAttrTag(attrTag);
        deviceAttr.setAttrId(attrId);
        deviceAttrMapper.insert(deviceAttr);
    }
}
