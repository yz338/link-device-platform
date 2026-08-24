package com.devicemanagement.module.alarm.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.devicemanagement.module.alarm.bean.Alarm;
import com.devicemanagement.module.alarm.bean.AlarmIndicator;
import com.devicemanagement.module.alarm.bean.AlarmRule;
import com.devicemanagement.module.alarm.bean.AlarmRuleCondition;
import com.devicemanagement.module.alarm.mapper.*;
import com.devicemanagement.module.device.entity.Device;
import com.devicemanagement.module.device.entity.DeviceAttr;
import com.devicemanagement.module.device.entity.DeviceCategory;
import com.devicemanagement.module.alarm.bean.IndicatorDevice;
import java.util.stream.Collectors;

import com.devicemanagement.module.device.mapper.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlarmDetectTask {

    private final DeviceMapper deviceMapper;
    private final DeviceAttrMapper deviceAttrMapper;
    private final DeviceCategoryMapper deviceCategoryMapper;
    private final AlarmMapper alarmMapper;
    private final AlarmRuleMapper alarmRuleMapper;
    private final AlarmIndicatorMapper alarmIndicatorMapper;
    private final AlarmRuleConditionMapper ruleConditionMapper;

    private final IndicatorDeviceMapper indicatorDeviceMapper;
    private final DeviceSubmissionElectricityMapper electricityMapper;
    private final DeviceSubmissionAirStationMapper airStationMapper;
    private final DeviceSubmissionSurfaceWaterMapper surfaceWaterMapper;
    private final DeviceSubmissionTvocMapper tvocMapper;

    /**
     * 告警类别 type：1=离线（含设备离线+无数据），2=数据异常
     */
    private static final int TYPE_OFFLINE = 1;
    private static final int TYPE_DATA_EXCEPTION = 2;

    @Scheduled(fixedRate = 180000)
    @Async("deviceTaskExecutor")
    public void detect() {
        log.info("开始执行告警检测...");

        // 1. 查所有设备
        List<Device> devices = deviceMapper.selectList(new LambdaQueryWrapper<>());
        long fiveMinAgo = System.currentTimeMillis() - 5 * 60 * 1000L;

        for (Device device : devices) {
            try {
                // 2. 查 device_attr 映射
                DeviceAttr attr = deviceAttrMapper.selectOne(
                        new LambdaQueryWrapper<DeviceAttr>()
                                .eq(DeviceAttr::getDeviceId, device.getId()));
                if (attr == null) continue;

                // 3. 查设备类别
                String categoryName = getCategoryName(device.getDeviceCategoryId());

                // 4. 离线设备告警（status = 0）
                if (device.getStatus() == null || device.getStatus() == 0) {
                    createAlarm(device, "离线", categoryName, null, TYPE_OFFLINE);
                    continue;
                }

                // 5. 取最新一条报送记录
                Object latestRecord = getLatestRecord(attr.getAttrTag(), attr.getAttrId());
                Date latestTime = getLatestTime(latestRecord);

                // 6. 5 分钟内没有发数据 → 归为离线
                if (latestTime == null || latestTime.getTime() < fiveMinAgo) {
                    createAlarm(device, "离线", categoryName, null, TYPE_OFFLINE);
                    continue;
                }

                // 7. 按告警规则检查数据异常
                checkAndCreateDataAlarm(device, categoryName, latestRecord);

            } catch (Exception e) {
                log.error("告警检测失败, deviceId={}", device.getId(), e);
            }
        }
        log.info("告警检测完成");
    }

    private String getCategoryName(Long categoryId) {
        if (categoryId == null) return "未知";
        DeviceCategory category = deviceCategoryMapper.selectById(categoryId);
        return category != null ? category.getCategoryName() : "未知";
    }

    /**
     * 根据 attrTag 获取设备最新报送记录（返回完整实体，用于取 data_time + 各指标字段）
     */
    private Object getLatestRecord(String attrTag, Long attrId) {
        if (attrTag == null || attrId == null) return null;
        String mn = String.valueOf(attrId);
        List<?> list;
        switch (attrTag) {
            case "electric_meter": list = electricityMapper.selectTop10ByMn(mn);   break;
            case "air_station":    list = airStationMapper.selectTop10ByMn(mn);    break;
            case "surface_water":  list = surfaceWaterMapper.selectTop10ByMn(mn);  break;
            case "tvoc":           list = tvocMapper.selectTop10ByMn(mn);           break;
            default:               return null;
        }
        return (list != null && !list.isEmpty()) ? list.get(0) : null;
    }

    /**
     * 从报送记录里提取 data_time
     */
    private Date getLatestTime(Object record) {
        if (record == null) return null;
        try {
            Method m = record.getClass().getMethod("getDataTime");
            return (Date) m.invoke(record);
        } catch (Exception e) {
            return null;
        }
    }

    // ============================================================
    // 数据异常检查
    // ============================================================

    private void checkAndCreateDataAlarm(Device device, String categoryName, Object latestRecord) {
        // 1. 通过 indicator_device 表查：这台设备被哪些告警指标选中了
        List<Long> indicatorIds = indicatorDeviceMapper.selectList(
                        new LambdaQueryWrapper<IndicatorDevice>()
                                .eq(IndicatorDevice::getDeviceId, device.getId()))
                .stream()
                .map(IndicatorDevice::getIndicatorId)
                .distinct()
                .collect(Collectors.toList());
        if (indicatorIds == null || indicatorIds.isEmpty()) {
            return; // 该设备没有配置任何告警指标，跳过
        }

        // 2. 查询这些指标下启用中的告警规则（规则不再管设备，只管指标引用）
        List<AlarmRule> rules = alarmRuleMapper.selectList(
                new LambdaQueryWrapper<AlarmRule>()
                        .eq(AlarmRule::getStatus, 1)
                        .in(AlarmRule::getAlarmIndicatorId, indicatorIds));
        if (rules == null || rules.isEmpty()) {
            return;
        }

        for (AlarmRule rule : rules) {
            List<AlarmRuleCondition> conds = ruleConditionMapper.selectList(
                    new LambdaQueryWrapper<AlarmRuleCondition>()
                            .eq(AlarmRuleCondition::getRuleId, rule.getId())
                            .orderByAsc(AlarmRuleCondition::getSort));

            boolean hit = checkConditions(conds, rule.getRuleRelation(),
                    rule.getTopConditionRelation(), latestRecord);
            if (!hit) continue;

            // 命中 → 生成数据异常告警，内容取对应指标名
            AlarmIndicator indicator = alarmIndicatorMapper.selectById(rule.getAlarmIndicatorId());
            String content = indicator != null ? indicator.getIndicatorName() : "数据异常";
            createAlarm(device, content, categoryName,
                    indicator != null ? indicator.getId() : null, TYPE_DATA_EXCEPTION);
        }
    }

    /**
     * 按规则关系检查条件是否命中
     */
    private boolean checkConditions(List<AlarmRuleCondition> conds, String ruleRelation,
                                    String topCondition, Object record) {
        if (conds == null || conds.isEmpty() || record == null) return false;

        // 按 parentId 分组：parentId=null 按 0 分组
        Map<Long, List<AlarmRuleCondition>> groups = new LinkedHashMap<>();
        for (AlarmRuleCondition c : conds) {
            Long pid = c.getParentId() == null ? 0L : c.getParentId();
            groups.computeIfAbsent(pid, k -> new ArrayList<>()).add(c);
        }

        // 组间（顶层）关系
        boolean topResult = "AND".equalsIgnoreCase(topCondition);

        for (Map.Entry<Long, List<AlarmRuleCondition>> entry : groups.entrySet()) {
            List<AlarmRuleCondition> group = entry.getValue();
            // 组内关系
            boolean groupResult = "AND".equalsIgnoreCase(ruleRelation);

            for (AlarmRuleCondition c : group) {
                BigDecimal val = getFieldValue(record, c.getAlarmAttribute());
                if (val == null) {
                    if ("AND".equalsIgnoreCase(ruleRelation)) {
                        groupResult = false;
                        break;
                    }
                    continue;
                }
                boolean match = compare(val, c.getOperator(), c.getThreshold());
                if ("AND".equalsIgnoreCase(ruleRelation)) {
                    groupResult = groupResult && match;
                    if (!groupResult) break;
                } else {
                    groupResult = groupResult || match;
                    if (groupResult) break;
                }
            }

            if ("AND".equalsIgnoreCase(topCondition)) {
                topResult = topResult && groupResult;
                if (!topResult) break;
            } else {
                topResult = topResult || groupResult;
                if (topResult) break;
            }
        }
        return topResult;
    }

    /**
     * 反射从报送实体取对应字段值
     */
    private BigDecimal getFieldValue(Object record, String fieldName) {
        if (record == null || fieldName == null) return null;
        try {
            String cap = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            Method m = record.getClass().getMethod("get" + cap);
            Object v = m.invoke(record);
            if (v == null) return null;
            if (v instanceof BigDecimal) return (BigDecimal) v;
            if (v instanceof String) {
                String s = ((String) v).trim();
                return s.isEmpty() ? null : new BigDecimal(s);
            }
            if (v instanceof Number) return new BigDecimal(v.toString());
        } catch (Exception ignored) {}
        return null;
    }

    /**
     * 数值比较
     */
    private boolean compare(BigDecimal value, String operator, BigDecimal threshold) {
        if (value == null || operator == null || threshold == null) return false;
        int c = value.compareTo(threshold);
        switch (operator) {
            case ">":  return c > 0;
            case "<":  return c < 0;
            case ">=": return c >= 0;
            case "<=": return c <= 0;
            case "=":  return c == 0;
            default:   return false;
        }
    }

    // ============================================================
    // 告警落库
    // ============================================================

    /**
     * 生成告警记录（同设备 + 同内容 + 同类型，存在未处理/处理中的不重复生成）
     */
    private void createAlarm(Device device, String content, String categoryName,
                             Long indicatorId, int type) {
        // 只要有未处理(0)或处理中(1)的同告警，就不重复生成
        Long count = alarmMapper.selectCount(new LambdaQueryWrapper<Alarm>()
                .eq(Alarm::getDeviceId, device.getId())
                .eq(Alarm::getAlarmContent, content)
                .eq(Alarm::getType, type)
                .in(Alarm::getStatus, 0, 1));
        if (count != null && count > 0) {
            return;
        }

        Alarm alarm = new Alarm();
        alarm.setAlarmDevice(device.getDeviceName());
        alarm.setDeviceId(device.getId());
        alarm.setAlarmContent(content);
        alarm.setDeviceCategory(categoryName);
        alarm.setAlarmTime(new Date());
        alarm.setStatus(0);
        alarm.setType(type);
        if (indicatorId != null) {
            alarm.setAlarmIndicatorId(indicatorId);
        }
        alarmMapper.insert(alarm);
    }

}