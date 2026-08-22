package com.devicemanagement.module.alarm.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.devicemanagement.common.Result;
import com.devicemanagement.module.alarm.bean.AlarmRule;
import com.devicemanagement.module.alarm.dto.AlarmRuleSaveDTO;
import com.devicemanagement.module.alarm.service.AlarmRuleService;
import com.devicemanagement.module.alarm.vo.AlarmRuleVO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
@RestController
@RequestMapping("/alarmRule")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AlarmRuleController {
    private final AlarmRuleService alarmRuleService;
    @PostMapping("/rule/save")
    @Operation(summary = "新增/编辑告警规则")
    public Result<String> saveRule(@RequestBody @Valid AlarmRuleSaveDTO dto) {
        alarmRuleService.saveRule(dto);
        return Result.success("保存成功");
    }

    @GetMapping("/rule/page")
    @Operation(summary = "分页查询告警规则")
    public Result<Page<AlarmRuleVO>> getRulePage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(alarmRuleService.getRulePage(pageNum, pageSize));
    }

    @DeleteMapping("/rule/delete")
    @Operation(summary = "删除告警规则")
    public Result<String> deleteRule(@RequestParam Long id) {
        alarmRuleService.deleteRule(id);
        return Result.success("删除成功");
    }
}
