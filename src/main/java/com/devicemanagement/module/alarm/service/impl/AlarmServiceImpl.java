package com.devicemanagement.module.alarm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.devicemanagement.module.alarm.bean.Alarm;
import com.devicemanagement.module.alarm.bean.AlarmIndicator;
import com.devicemanagement.module.alarm.dto.AlarmQueryDTO;
import com.devicemanagement.module.alarm.mapper.AlarmIndicatorMapper;
import com.devicemanagement.module.alarm.mapper.AlarmMapper;
import com.devicemanagement.module.alarm.service.AlarmService;
import com.devicemanagement.module.alarm.vo.AlarmDetailVO;
import com.devicemanagement.module.alarm.vo.AlarmVO;
import com.devicemanagement.module.device.entity.Device;
import com.devicemanagement.module.device.entity.DeviceAttr;
import com.devicemanagement.module.device.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlarmServiceImpl extends ServiceImpl<AlarmMapper, Alarm> implements AlarmService {

    private final AlarmIndicatorMapper alarmIndicatorMapper;

    // 设备相关 Mapper
    private final DeviceAttrMapper deviceAttrMapper;
    private final DeviceSubmissionElectricityMapper electricityMapper;
    private final DeviceSubmissionAirStationMapper airStationSubmissionMapper;
    private final DeviceSubmissionSurfaceWaterMapper surfaceWaterSubmissionMapper;
    private final DeviceSubmissionTvocMapper tvocSubmissionMapper;

    private final DeviceMapper deviceMapper;

    @Override
    public List<Alarm> getAlarmsByDeviceId(Long deviceId) {
        return this.list(new LambdaQueryWrapper<Alarm>()
                .eq(Alarm::getDeviceId, deviceId)
                .orderByDesc(Alarm::getAlarmTime));
    }

    @Override
    public AlarmVO getAlarmDetail(Long id) {
        Alarm alarm = this.getById(id);
        if (alarm == null) {
            return null;
        }
        AlarmVO vo = new AlarmVO();
        BeanUtils.copyProperties(alarm, vo);
        if (alarm.getAlarmIndicatorId() != null) {
            AlarmIndicator indicator = alarmIndicatorMapper.selectById(alarm.getAlarmIndicatorId());
            vo.setAlarmIndicator(indicator);
        }
        if (alarm.getDeviceId() != null) {
            DeviceAttr deviceAttr = deviceAttrMapper.selectOne(
                    new LambdaQueryWrapper<DeviceAttr>()
                            .eq(DeviceAttr::getDeviceId, alarm.getDeviceId()));
            if (deviceAttr != null) {
                String attrTag = deviceAttr.getAttrTag();
                String mn = String.valueOf(deviceAttr.getAttrId());
                List<?> records = new ArrayList<>();
                switch (attrTag) {
                    case "electric_meter":
                        records = electricityMapper.selectTop10ByMn(mn);
                        break;
                    case "air_station":
                        records = airStationSubmissionMapper.selectTop10ByMn(mn);
                        break;
                    case "surface_water":
                        records = surfaceWaterSubmissionMapper.selectTop10ByMn(mn);
                        break;
                    case "tvoc":
                        records = tvocSubmissionMapper.selectTop10ByMn(mn);
                        break;
                }
                vo.setData(records);
            }
        }
        return vo;
    }
    @Override
    public Page<AlarmDetailVO> getAlarmPage(AlarmQueryDTO dto) {
        // 1. 构建告警查询条件
        LambdaQueryWrapper<Alarm> wrapper = new LambdaQueryWrapper<>();
        if (dto.getStatus() != null) {
            wrapper.eq(Alarm::getStatus, dto.getStatus());
        }
        if (dto.getDeviceName() != null) {
            wrapper.like(Alarm::getAlarmDevice, dto.getDeviceName());
        }
        if (dto.getDeviceCategory() != null) {
            wrapper.eq(Alarm::getDeviceCategory, dto.getDeviceCategory());
        }
        wrapper.orderByDesc(Alarm::getAlarmTime);
        // 2. 分页查询告警
        Page<Alarm> page = this.page(new Page<>(dto.getPageNum(), dto.getPageSize()), wrapper);
        // 3. 转 VO，关联查设备信息
        List<AlarmDetailVO> records = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Alarm alarm : page.getRecords()) {
            AlarmDetailVO vo = new AlarmDetailVO();
            vo.setId(alarm.getId());
            vo.setDeviceName(alarm.getAlarmDevice());
            vo.setAlarmContent(alarm.getAlarmContent());
            vo.setDeviceCategory(alarm.getDeviceCategory());
            vo.setStatus(alarm.getStatus());
            vo.setHandler(alarm.getHandler());
            vo.setAlarmTime(alarm.getAlarmTime() != null ? sdf.format(alarm.getAlarmTime()) : null);
            // 关联查设备信息
            if (alarm.getDeviceId() != null) {
                Device device = deviceMapper.selectById(alarm.getDeviceId());
                if (device != null) {
                    vo.setDeviceId(device.getId());
                    vo.setDeviceAddress(device.getLocation());
                }
            }
            records.add(vo);
        }
        // 4. 返回分页结果
        Page<AlarmDetailVO> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        result.setRecords(records);
        return result;
    }

    @Override
    public Long countUnhand() {
        LambdaQueryWrapper<Alarm> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Alarm::getStatus, 1);
        return this.count(wrapper);
    }

    @Override
    public Long countHand() {
        LambdaQueryWrapper<Alarm> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Alarm::getStatus, 3);
        return this.count(wrapper);
    }
}