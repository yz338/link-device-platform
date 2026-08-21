package com.devicemanagement.module.alarm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.devicemanagement.module.alarm.bean.AlarmIndicator;
import com.devicemanagement.module.alarm.bean.AlarmRule;
import com.devicemanagement.module.alarm.bean.AlarmRuleCondition;
import com.devicemanagement.module.alarm.dto.AlarmRuleSaveDTO;
import com.devicemanagement.module.alarm.mapper.AlarmIndicatorMapper;
import com.devicemanagement.module.alarm.mapper.AlarmRuleConditionMapper;
import com.devicemanagement.module.alarm.mapper.AlarmRuleMapper;
import com.devicemanagement.module.alarm.service.AlarmRuleService;
import com.devicemanagement.module.alarm.vo.AlarmRuleVO;
import com.devicemanagement.module.device.entity.DeviceCategory;
import com.devicemanagement.module.device.mapper.DeviceCategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlarmRuleServiceImpl extends ServiceImpl<AlarmRuleMapper, AlarmRule> implements AlarmRuleService {

    private final AlarmRuleConditionMapper conditionMapper;
    private final DeviceCategoryMapper deviceCategoryMapper;
    private final AlarmIndicatorMapper indicatorMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRule(AlarmRuleSaveDTO dto) {
        AlarmRule rule;
        if (dto.getId() != null) {
            rule = this.getById(dto.getId());
            conditionMapper.delete(new LambdaQueryWrapper<AlarmRuleCondition>()
                    .eq(AlarmRuleCondition::getRuleId, dto.getId()));
        } else {
            rule = new AlarmRule();
            rule.setStatus((byte)1);
            rule.setIsDeleted((byte)0);
        }
        BeanUtils.copyProperties(dto, rule, "status", "isDeleted", "createTime", "updateTime", "createBy");
// ↓↓↓ 新增才 save；编辑的 save 在 P0-6 那里 updateById
        if (dto.getId() == null) {
            this.save(rule);
        }
        for (AlarmRuleSaveDTO.ConditionItem itemDTO : dto.getConditions()) {
            AlarmRuleCondition cond = new AlarmRuleCondition();
            BeanUtils.copyProperties(itemDTO, cond);
            cond.setRuleId(rule.getId());
            conditionMapper.insert(cond);
        }
        if (dto.getId() != null) {
            this.updateById(rule);   // 编辑才 update，新增 save 在下条 P1 说
        }
    }

    @Override
    public Page<AlarmRuleVO> getRulePage(Integer pageNum, Integer pageSize) {
        Page<AlarmRule> page = this.page(new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<AlarmRule>()
                        .orderByDesc(AlarmRule::getCreateTime));

        List<AlarmRuleVO> voList = new ArrayList<>();
        for (AlarmRule rule : page.getRecords()) {
            AlarmRuleVO vo = new AlarmRuleVO();
            BeanUtils.copyProperties(rule, vo);

            List<AlarmRuleCondition> conditions = conditionMapper.selectList(
                    new LambdaQueryWrapper<AlarmRuleCondition>()
                            .eq(AlarmRuleCondition::getRuleId, rule.getId())
                            .orderByAsc(AlarmRuleCondition::getSort));
            vo.setConditions(conditions);

            if (rule.getDeviceCategoryId() != null) {
                DeviceCategory category = deviceCategoryMapper.selectById(rule.getDeviceCategoryId());
                if (category != null) {
                    vo.setDeviceCategoryName(category.getCategoryName());
                }
            }

            if (rule.getAlarmIndicatorId() != null) {
                AlarmIndicator indicator = indicatorMapper.selectById(rule.getAlarmIndicatorId());
                if (indicator != null) {
                    vo.setAlarmIndicatorName(indicator.getIndicatorName());
                }
            }
            voList.add(vo);
        }

        Page<AlarmRuleVO> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        result.setRecords(voList);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deleteRule(Long id) {
        conditionMapper.delete(new LambdaQueryWrapper<AlarmRuleCondition>()
                .eq(AlarmRuleCondition::getRuleId, id));
        this.removeById(id);
        return "删除成功";
    }
}