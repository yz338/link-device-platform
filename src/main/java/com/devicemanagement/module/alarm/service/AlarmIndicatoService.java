package com.devicemanagement.module.alarm.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.devicemanagement.module.alarm.bean.AlarmIndicator;
import com.devicemanagement.module.alarm.dto.AlarmIndicatorDTO;
import com.devicemanagement.module.alarm.vo.AlarmIndicatorVO;
import com.devicemanagement.module.alarm.vo.AlarmVO;

import java.util.List;

public interface AlarmIndicatoService extends IService<AlarmIndicator> {
    Page<AlarmIndicatorVO> getAlarmIndicator(AlarmIndicatorDTO alarmIndicatorDTO);
    String deleteAlarmIndicator(Long id);
    String addAlarmIndicator(AlarmIndicatorDTO alarmIndicatorDTO);
    String updateAlarmIndicator(Long id,String name,String description);
    /** 编辑告警指标（支持修改状态）：status 传 null 则不修改状态 */
    String updateAlarmIndicator(Long id,String name,String description,Byte status);
}
