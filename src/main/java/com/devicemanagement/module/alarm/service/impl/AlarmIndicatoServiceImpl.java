package com.devicemanagement.module.alarm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.devicemanagement.module.alarm.bean.Alarm;
import com.devicemanagement.module.alarm.bean.AlarmIndicator;
import com.devicemanagement.module.alarm.bean.AlarmRule;
import com.devicemanagement.module.alarm.bean.IndicatorDevice;
import com.devicemanagement.module.alarm.dto.AlarmIndicatorDTO;
import com.devicemanagement.module.alarm.mapper.AlarmIndicatorMapper;
import com.devicemanagement.module.alarm.mapper.AlarmMapper;
import com.devicemanagement.module.alarm.mapper.AlarmRuleMapper;
import com.devicemanagement.module.alarm.mapper.IndicatorDeviceMapper;
import com.devicemanagement.module.alarm.service.AlarmIndicatoService;
import com.devicemanagement.module.alarm.vo.AlarmIndicatorVO;
import com.devicemanagement.module.device.entity.Device;
import com.devicemanagement.module.device.entity.DeviceCategory;
import com.devicemanagement.module.device.mapper.DeviceCategoryMapper;
import com.devicemanagement.module.device.mapper.DeviceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AlarmIndicatoServiceImpl extends ServiceImpl<AlarmIndicatorMapper, AlarmIndicator> implements AlarmIndicatoService {

    private final DeviceCategoryMapper deviceCategoryMapper;
    private final DeviceMapper deviceMapper;
    private final AlarmMapper alarmMapper;
    private final AlarmRuleMapper alarmRuleMapper;
    private final IndicatorDeviceMapper indicatorDeviceMapper;

    @Override
    public Page<AlarmIndicatorVO> getAlarmIndicator(AlarmIndicatorDTO alarmIndicatorDTO) {
        LambdaQueryWrapper<AlarmIndicator> wrapper = new LambdaQueryWrapper<>();

        if (alarmIndicatorDTO.getStatus() != null) {
            wrapper.eq(AlarmIndicator::getStatus, alarmIndicatorDTO.getStatus());
        }
        if (alarmIndicatorDTO.getIndicatorName() != null) {
            wrapper.like(AlarmIndicator::getIndicatorName, alarmIndicatorDTO.getIndicatorName());
        }
        if (alarmIndicatorDTO.getDeviceCategoryName() != null) {
            LambdaQueryWrapper<DeviceCategory> categoryWrapper = new LambdaQueryWrapper<>();
            categoryWrapper.eq(DeviceCategory::getCategoryName, alarmIndicatorDTO.getDeviceCategoryName());
            DeviceCategory deviceCategory = deviceCategoryMapper.selectOne(categoryWrapper);
            if (deviceCategory != null) {
                wrapper.eq(AlarmIndicator::getDeviceCategoryId, deviceCategory.getId());
            } else {
                return new Page<>(alarmIndicatorDTO.getPageNum(), alarmIndicatorDTO.getPageSize(), 0);
            }
        }

        Page<AlarmIndicator> page = baseMapper.selectPage(
                new Page<>(alarmIndicatorDTO.getPageNum(), alarmIndicatorDTO.getPageSize()), wrapper);

        List<AlarmIndicatorVO> voList = new ArrayList<>();
        for (AlarmIndicator indicator : page.getRecords()) {
            AlarmIndicatorVO vo = new AlarmIndicatorVO();
            BeanUtils.copyProperties(indicator, vo);
            if (indicator.getDeviceCategoryId() != null) {
                DeviceCategory category = deviceCategoryMapper.selectById(indicator.getDeviceCategoryId());
                if (category != null) {
                    vo.setDeviceCategoryName(category.getCategoryName());
                }
            }
            voList.add(vo);
        }

        Page<AlarmIndicatorVO> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        result.setRecords(voList);
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public String addAlarmIndicator(AlarmIndicatorDTO dto) {
        // 1. 查类别（必填）
        LambdaQueryWrapper<DeviceCategory> cw = new LambdaQueryWrapper<>();
        cw.eq(DeviceCategory::getCategoryName, dto.getDeviceCategoryName());
        DeviceCategory category = deviceCategoryMapper.selectOne(cw);
        if (category == null) return "设备类别不存在";

        // 2. 校验 deviceIds 是否属于该类别
        List<Long> ids = dto.getDeviceIds().stream().distinct().collect(Collectors.toList());
        List<Device> devices = deviceMapper.selectBatchIds(ids);
        if (devices.size() != ids.size()) return "存在无效的设备ID";
        for (Device d : devices) {
            if (!category.getId().equals(d.getDeviceCategoryId())) {
                return "设备【" + d.getDeviceName() + "】不属于所选类别";
            }
        }

        // 3. 保存指标（deviceId = null 不存单设备，作用对象走 indicator_device 表）
        AlarmIndicator alarmIndicator = new AlarmIndicator();
        BeanUtils.copyProperties(dto, alarmIndicator);
        alarmIndicator.setStatus((byte) 1);
        alarmIndicator.setDeviceCategoryId(category.getId());
        alarmIndicator.setDeviceId(null);
        this.save(alarmIndicator);

        // 4. 批量保存指标-设备关联
        for (Long deviceId : ids) {
            IndicatorDevice idr = new IndicatorDevice();
            idr.setIndicatorId(alarmIndicator.getId());
            idr.setDeviceId(deviceId);
            indicatorDeviceMapper.insert(idr);
        }
        return "新增成功";
    }

    @Transactional(rollbackFor = Exception.class)
    public String updateAlarmIndicator(AlarmIndicatorDTO dto) {
        if (dto.getId() == null) return "指标ID不能为空";
        // 删旧关联
        indicatorDeviceMapper.delete(new LambdaQueryWrapper<IndicatorDevice>()
                .eq(IndicatorDevice::getIndicatorId, dto.getId()));
        // 更新指标本身
        AlarmIndicator ai = this.getById(dto.getId());
        if (ai == null) return "指标不存在";
        BeanUtils.copyProperties(dto, ai);
        DeviceCategory category = deviceCategoryMapper.selectOne(
                new LambdaQueryWrapper<DeviceCategory>()
                        .eq(DeviceCategory::getCategoryName, dto.getDeviceCategoryName()));
        if (category == null) return "设备类别不存在";
        ai.setDeviceCategoryId(category.getId());
        this.updateById(ai);
        // 重新插关联
        for (Long deviceId : dto.getDeviceIds().stream().distinct().collect(Collectors.toList())) {
            IndicatorDevice idr = new IndicatorDevice();
            idr.setIndicatorId(dto.getId());
            idr.setDeviceId(deviceId);
            indicatorDeviceMapper.insert(idr);
        }
        return "更新成功";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deleteAlarmIndicator(Long id) {
        if (id == null) throw new IllegalArgumentException("指标ID不能为空");

        Long ruleCount = alarmRuleMapper.selectCount(
                new LambdaQueryWrapper<AlarmRule>()
                        .eq(AlarmRule::getAlarmIndicatorId, id)
                        .eq(AlarmRule::getStatus, 1));
        if (ruleCount != null && ruleCount > 0) {
            return "该指标已被 " + ruleCount + " 条启用中的告警规则引用，无法删除";
        }
        Long processingCount = alarmMapper.selectCount(
                new LambdaQueryWrapper<Alarm>()
                        .eq(Alarm::getAlarmIndicatorId, id)
                        .eq(Alarm::getStatus, 1));
        if (processingCount != null && processingCount > 0) {
            return "该指标下存在 " + processingCount + " 条处理中的告警工单，请先完成处理后再删除";
        }
        // 删 indicator_device 关联
        indicatorDeviceMapper.delete(new LambdaQueryWrapper<IndicatorDevice>()
                .eq(IndicatorDevice::getIndicatorId, id));
        boolean success = this.removeById(id);
        return success ? "删除成功" : "删除失败，指标可能不存在";
    }

    @Override
    public String updateAlarmIndicator(Long id, String name, String description) {
        AlarmIndicator alarmIndicator = this.getById(id);
        if (alarmIndicator == null) return "指标不存在";
        alarmIndicator.setIndicatorName(name);
        alarmIndicator.setDescription(description);
        return this.updateById(alarmIndicator) ? "修改成功" : "修改失败";
    }
}
