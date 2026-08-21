package com.devicemanagement.module.device.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.devicemanagement.module.device.dto.DeviceAttrUpdateDTO;
import com.devicemanagement.module.device.dto.DeviceCategoryDTO;
import com.devicemanagement.module.device.dto.DeviceDTO;
import com.devicemanagement.module.device.entity.*;
import com.devicemanagement.module.device.entity.pulldata.DeviceSubmissionAirStation;
import com.devicemanagement.module.device.entity.pulldata.DeviceSubmissionElectricity;
import com.devicemanagement.module.device.entity.pulldata.DeviceSubmissionSurfaceWater;
import com.devicemanagement.module.device.entity.pulldata.DeviceSubmissionTvoc;
import com.devicemanagement.module.device.mapper.*;
import com.devicemanagement.module.device.service.DeviceCategoryService;
import com.devicemanagement.module.device.service.DeviceService;
import com.devicemanagement.module.device.vo.DeviceDetailVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
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
    @Transactional(rollbackFor = Exception.class)
    public void save(DeviceDTO deviceDTO) {
        Device device = new Device();
        BeanUtils.copyProperties(deviceDTO, device);
        this.save(device);
        createDeviceAttrForDevice(device);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Device updateDevice(DeviceDTO deviceDTO) {
        if (deviceDTO.getId() == null) {
            throw new IllegalArgumentException("设备ID不能为空");
        }
        LambdaUpdateWrapper<Device> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Device::getId, deviceDTO.getId());
        // 类别名校选填，传了才更新
        if (deviceDTO.getDeviceCategoryName() != null) {
            LambdaQueryWrapper<DeviceCategory> queryWrapper = new LambdaQueryWrapper<>();
            DeviceCategory deviceCategory = deviceCategoryService.getOne(
                    queryWrapper.eq(DeviceCategory::getCategoryName, deviceDTO.getDeviceCategoryName()));
            if (deviceCategory == null) {
                throw new IllegalArgumentException("设备类别不存在: " + deviceDTO.getDeviceCategoryName());
            }
            updateWrapper.set(Device::getDeviceCategoryId, deviceCategory.getId());
        }
        updateWrapper.set(Device::getDeviceName,deviceDTO.getDeviceName());
        updateWrapper.set(Device::getLocation,deviceDTO.getLocation());
        updateWrapper.set(Device::getStatus,deviceDTO.getStatus());
        this.update(updateWrapper);
        return this.getById(deviceDTO.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deleteDeviceById(Long id) {
        if (id == null) {
            return "设备ID不能为空";
        }
        Device device = deviceMapper.selectById(id);
        if (device == null) {
            return "设备不存在";
        }
        DeviceAttr deviceAttr = deviceAttrMapper.selectOne(
                new LambdaQueryWrapper<DeviceAttr>().eq(DeviceAttr::getDeviceId, id));
        if (deviceAttr != null) {
            String attrTag = deviceAttr.getAttrTag();
            Long attrId   = deviceAttr.getAttrId();
            switch (attrTag) {
                case "electric_meter":
                    electricMeterMapper.deleteById(attrId);
                    electricityMapper.delete(new LambdaQueryWrapper<DeviceSubmissionElectricity>()
                            .eq(DeviceSubmissionElectricity::getMn, String.valueOf(attrId)));
                    break;
                case "air_station":
                    airStationMapper.deleteById(attrId);
                    airStationSubmissionMapper.delete(new LambdaQueryWrapper<DeviceSubmissionAirStation>()
                            .eq(DeviceSubmissionAirStation::getMn, String.valueOf(attrId)));
                    break;
                case "surface_water":
                    surfaceWaterMapper.deleteById(attrId);
                    surfaceWaterSubmissionMapper.delete(new LambdaQueryWrapper<DeviceSubmissionSurfaceWater>()
                            .eq(DeviceSubmissionSurfaceWater::getMn, String.valueOf(attrId)));
                    break;
                case "tvoc":
                    tvocMapper.deleteById(attrId);
                    tvocSubmissionMapper.delete(new LambdaQueryWrapper<DeviceSubmissionTvoc>()
                            .eq(DeviceSubmissionTvoc::getMn, String.valueOf(attrId)));
                    break;
            }
            deviceAttrMapper.deleteById(deviceAttr.getId());
        }
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
        DeviceDetailVO vo = deviceMapper.selectDetailById(id);
        if (vo == null) {
            return null;
        }
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
        vo.setLatestSubmitTime(getLatestSubmitTime(vo.getAttrTag(), id));
        List<?> records = new ArrayList<>();
        if (vo.getAttrTag() != null && vo.getAttrId() != null) {
            switch (vo.getAttrTag()) {
                case "electric_meter" :
                    records = electricityMapper.selectTop10ByMn(String.valueOf(vo.getAttrId()));
                    break;
                case "air_station" :
                    records = airStationSubmissionMapper.selectTop10ByMn(String.valueOf(vo.getAttrId()));
                    break;
                case "surface_water":
                    records = surfaceWaterSubmissionMapper.selectTop10ByMn(String.valueOf(vo.getAttrId()));
                    break;
                case "tvoc":
                    records = tvocSubmissionMapper.selectTop10ByMn(String.valueOf(vo.getAttrId()));
                    break;
            }
            vo.setData(records);
        }
        return vo;
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDeviceAttr(DeviceAttrUpdateDTO dto) {
        if (dto.getDeviceId() == null || dto.getAttributes() == null || dto.getAttributes().isEmpty()) {
            return;
        }
        DeviceAttr deviceAttr = deviceAttrMapper.selectOne(
                new LambdaQueryWrapper<DeviceAttr>()
                        .eq(DeviceAttr::getDeviceId, dto.getDeviceId()));
        if (deviceAttr == null) {
            return;
        }
        String attrTag = deviceAttr.getAttrTag();
        Long attrId = deviceAttr.getAttrId();

        switch (attrTag) {
            case "electric_meter":
                updateElectricMeter(attrId,dto.getAttributes());
                break;
            case "air_station":
                updateAirStation(attrId, dto.getAttributes());
                break;
            case "surface_water":
                updateSurfaceWater(attrId, dto.getAttributes());
                break;
            case "tvoc":
                updateTvoc(attrId, dto.getAttributes());
                break;
        }
    }

    private void updateElectricMeter(Long attrId, Map<String, Object> attrs) {
        UpdateWrapper<DeviceAttrElectricMeter> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", attrId);
        for (Map.Entry<String, Object> entry : attrs.entrySet()) {
            wrapper.set(entry.getKey(), entry.getValue());
        }
        electricMeterMapper.update(null, wrapper);
    }

    private void updateAirStation(Long attrId, Map<String, Object> attrs) {
        UpdateWrapper<DeviceAttrAirStation> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", attrId);
        for (Map.Entry<String, Object> entry : attrs.entrySet()) {
            wrapper.set(entry.getKey(), entry.getValue());
        }
        airStationMapper.update(null, wrapper);
    }

    private void updateSurfaceWater(Long attrId, Map<String, Object> attrs) {
        UpdateWrapper<DeviceAttrSurfaceWater> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", attrId);
        for (Map.Entry<String, Object> entry : attrs.entrySet()) {
            wrapper.set(entry.getKey(), entry.getValue());
        }
        surfaceWaterMapper.update(null, wrapper);
    }

    private void updateTvoc(Long attrId, Map<String, Object> attrs) {
        UpdateWrapper<DeviceAttrTvoc> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", attrId);
        for (Map.Entry<String, Object> entry : attrs.entrySet()) {
            wrapper.set(entry.getKey(), entry.getValue());
        }
        tvocMapper.update(null, wrapper);
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

        Long mn = deviceAttr.getAttrId();
        switch (attrTag) {
            case "electric_meter":
                return electricityMapper.selectLatestSubmitTime(String.valueOf(mn));
            case "air_station":
                return airStationSubmissionMapper.selectLatestSubmitTime(String.valueOf(mn));
            case "surface_water":
                return surfaceWaterSubmissionMapper.selectLatestSubmitTime(String.valueOf(mn));
            case "tvoc":
                return tvocSubmissionMapper.selectLatestSubmitTime(String.valueOf(mn));
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
    /**
     * 在对应属性表创建带默认值的记录，返回主键 ID
     */
    private Long createEmptyAttrRecord(String attrTag) {
        switch (attrTag) {
            case "electric_meter":
                DeviceAttrElectricMeter electricMeter = new DeviceAttrElectricMeter();
                electricMeter.setCt_ratio(new BigDecimal("20"));
                electricMeter.setQ1eq(new BigDecimal("0"));
                electricMeter.setQ2eq(new BigDecimal("0"));
                electricMeter.setQ3eq(new BigDecimal("0"));
                electricMeter.setQ4eq(new BigDecimal("0"));
                electricMeter.setUab(new BigDecimal("380"));
                electricMeter.setUbc(new BigDecimal("380"));
                electricMeter.setUca(new BigDecimal("380"));
                electricMeter.setUa(new BigDecimal("220"));
                electricMeter.setUb(new BigDecimal("220"));
                electricMeter.setUc(new BigDecimal("220"));
                electricMeter.setIa(new BigDecimal("0"));
                electricMeter.setIb(new BigDecimal("0"));
                electricMeter.setIc(new BigDecimal("0"));
                electricMeter.setPt(new BigDecimal("0"));
                electricMeter.setPa(new BigDecimal("0"));
                electricMeter.setPb(new BigDecimal("0"));
                electricMeter.setPc(new BigDecimal("0"));
                electricMeter.setQt(new BigDecimal("0"));
                electricMeter.setQa(new BigDecimal("0"));
                electricMeter.setQb(new BigDecimal("0"));
                electricMeter.setQc(new BigDecimal("0"));
                electricMeter.setPft(new BigDecimal("0.9"));
                electricMeter.setPfa(new BigDecimal("0.9"));
                electricMeter.setPfb(new BigDecimal("0.9"));
                electricMeter.setPfc(new BigDecimal("0.9"));
                electricMeter.setFreq(new BigDecimal("50"));
                electricMeterMapper.insert(electricMeter);
                return electricMeter.getId();
            case "air_station":
                DeviceAttrAirStation airStation = new DeviceAttrAirStation();
                airStation.setSo2Min(new BigDecimal("0.02"));
                airStation.setSo2Avg(new BigDecimal("0.03"));
                airStation.setSo2Max(new BigDecimal("0.05"));
                airStation.setNoMin(new BigDecimal("0.01"));
                airStation.setNoAvg(new BigDecimal("0.02"));
                airStation.setNoMax(new BigDecimal("0.03"));
                airStation.setNo2Min(new BigDecimal("0.02"));
                airStation.setNo2Avg(new BigDecimal("0.03"));
                airStation.setNo2Max(new BigDecimal("0.04"));
                airStation.setNoxMin(new BigDecimal("0.03"));
                airStation.setNoxAvg(new BigDecimal("0.05"));
                airStation.setNoxMax(new BigDecimal("0.07"));
                airStation.setCoMin(new BigDecimal("0.5"));
                airStation.setCoAvg(new BigDecimal("1.0"));
                airStation.setCoMax(new BigDecimal("2.0"));
                airStation.setPm10Min(new BigDecimal("20"));
                airStation.setPm10Avg(new BigDecimal("35"));
                airStation.setPm10Max(new BigDecimal("50"));
                airStation.setPm25Min(new BigDecimal("10"));
                airStation.setPm25Avg(new BigDecimal("20"));
                airStation.setPm25Max(new BigDecimal("35"));
                airStationMapper.insert(airStation);
                return airStation.getId();

            case "surface_water":
                DeviceAttrSurfaceWater surfaceWater = new DeviceAttrSurfaceWater();
                surfaceWater.setWater_temperature(new BigDecimal("15"));
                surfaceWater.setPh_value(new BigDecimal("7"));
                surfaceWater.setDissolved_oxygen(new BigDecimal("8"));
                surfaceWater.setConductivity(new BigDecimal("500"));
                surfaceWater.setPermanganate_index(new BigDecimal("4"));
                surfaceWater.setAmmonia_nitrogen(new BigDecimal("0.5"));
                surfaceWater.setTotal_phosphorus(new BigDecimal("0.1"));
                surfaceWater.setTotal_nitrogen(new BigDecimal("1.0"));
                surfaceWaterMapper.insert(surfaceWater);
                return surfaceWater.getId();

            case "tvoc":
                DeviceAttrTvoc tvoc = new DeviceAttrTvoc();
                tvoc.setTemperature(new BigDecimal("25"));
                tvoc.setHumidity(new BigDecimal("50"));
                tvoc.setAir_pressure(new BigDecimal("101.3"));
                tvoc.setWind_speed(new BigDecimal("0.5"));
                tvoc.setWind_direction("东风");
                tvocMapper.insert(tvoc);
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
        DeviceCategory category = deviceCategoryMapper.selectById(device.getDeviceCategoryId());
        if (category == null) {
            return;
        }
        String attrTag = determineAttrTag(category.getCategoryName());
        if (attrTag == null) {
            return;
        }
        Long attrId = createEmptyAttrRecord(attrTag);
        if (attrId == null) {
            return;
        }
        DeviceAttr deviceAttr = new DeviceAttr();
        deviceAttr.setDeviceId(device.getId());
        deviceAttr.setAttrTag(attrTag);
        deviceAttr.setAttrId(attrId);
        deviceAttrMapper.insert(deviceAttr);
    }
}
