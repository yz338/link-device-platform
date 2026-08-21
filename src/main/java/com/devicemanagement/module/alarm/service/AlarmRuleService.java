package com.devicemanagement.module.alarm.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.devicemanagement.module.alarm.bean.AlarmRule;
import com.devicemanagement.module.alarm.dto.AlarmRuleSaveDTO;
import com.devicemanagement.module.alarm.vo.AlarmRuleVO;

public interface AlarmRuleService extends IService<AlarmRule> {
    public void saveRule(AlarmRuleSaveDTO dto);
    String deleteRule(Long id);
    Page<AlarmRuleVO> getRulePage(Integer pageNum, Integer pageSize);
}
