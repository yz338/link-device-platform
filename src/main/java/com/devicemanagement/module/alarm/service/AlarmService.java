package com.devicemanagement.module.alarm.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.devicemanagement.module.alarm.bean.Alarm;
import com.devicemanagement.module.alarm.dto.AlarmQueryDTO;
import com.devicemanagement.module.alarm.vo.AlarmDetailVO;
import com.devicemanagement.module.alarm.vo.AlarmVO;
import com.devicemanagement.module.device.entity.Device;

import java.util.List;

public interface AlarmService extends IService<Alarm> {
    List<Alarm> getAlarmsByDeviceId(Long deviceId);
    AlarmVO getAlarmDetail(Long id);
    Page<AlarmDetailVO> getAlarmPage(AlarmQueryDTO dto);
    Long countUnhand();
    Long countHand();
}
