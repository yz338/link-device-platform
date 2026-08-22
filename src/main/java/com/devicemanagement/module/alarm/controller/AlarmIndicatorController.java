package com.devicemanagement.module.alarm.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.devicemanagement.common.Result;
import com.devicemanagement.module.alarm.dto.AlarmIndicatorDTO;
import com.devicemanagement.module.alarm.service.AlarmIndicatoService;
import com.devicemanagement.module.alarm.vo.AlarmIndicatorVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/alarmIndicator")
@RequiredArgsConstructor
@Tag(name = "告警指标管理", description = "告警指标的增删改查")
@PreAuthorize("hasRole('ADMIN')")
public class AlarmIndicatorController {

    private final AlarmIndicatoService alarmIndicatoService;

    @GetMapping("/getAlarmIndicator")
    @Operation(summary = "分页查询告警指标", description = "支持按指标名称、设备类别、状态筛选")
    public Result<Page<AlarmIndicatorVO>> getAlarmIndicator(AlarmIndicatorDTO alarmIndicatorDTO) {
        return Result.success(alarmIndicatoService.getAlarmIndicator(alarmIndicatorDTO));
    }

    @DeleteMapping("/deleteAlarmIndicator")
    @Operation(summary = "删除告警指标")
    public Result<String> deleteAlarmIndicator(
            @Parameter(description = "告警指标ID", required = true) @RequestParam Long id) {
        return Result.success(alarmIndicatoService.deleteAlarmIndicator(id));
    }

    @PostMapping("/addAlarmIndicator")
    @Operation(summary = "新增告警指标", description = "同时批量保存作用设备（indicator_device关联表）")
    public Result<String> addAlarmIndicator(
            @RequestBody @Validated(AlarmIndicatorDTO.AddGroup.class) AlarmIndicatorDTO alarmIndicatorDTO) {
        return Result.success(alarmIndicatoService.addAlarmIndicator(alarmIndicatorDTO));
    }

    @PutMapping("/updateAlarmIndicator")
    @Operation(summary = "编辑告警指标（修改名称+描述+状态）")
    public Result<String> updateAlarmIndicator(
            @Parameter(description = "指标ID", required = true) @RequestParam Long id,
            @Parameter(description = "指标名称", required = true) @RequestParam String name,
            @Parameter(description = "指标描述") @RequestParam(required = false) String description,
            @Parameter(description = "状态：1=启用 2=停用") @RequestParam(required = false) Byte status) {
        return Result.success(alarmIndicatoService.updateAlarmIndicator(id, name, description, status));
    }
}