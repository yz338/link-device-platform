package com.devicemanagement.module.alarm.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.devicemanagement.common.Result;
import com.devicemanagement.module.alarm.bean.Alarm;
import com.devicemanagement.module.alarm.dto.AlarmQueryDTO;
import com.devicemanagement.module.alarm.service.AlarmService;
import com.devicemanagement.module.alarm.vo.AlarmDetailVO;
import com.devicemanagement.module.alarm.vo.AlarmVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/alarm")
@Tag(name = "告警管理", description = "告警列表、详情等接口")
public class AlarmController {
    @Autowired
    private AlarmService alarmService;

    @GetMapping("/getByDeviceId")
    @Operation(summary = "根据设备ID查询告警列表", description = "查询某设备的所有告警记录，按告警时间倒序")
    public Result<List<Alarm>> getAlarmsByDeviceId(
            @Parameter(description = "设备ID", required = true) @RequestParam Long deviceId) {
        return Result.success(alarmService.getAlarmsByDeviceId(deviceId));
    }

    @GetMapping("/getAlarmById")
    @Operation(summary = "获取告警详情", description = "根据告警ID查询详情，包含告警指标和推送数据")
    public Result<AlarmVO> getAlarmDetail(
            @Parameter(description = "告警ID", required = true) @RequestParam Long id) {
        return Result.success(alarmService.getAlarmDetail(id));
    }
    @GetMapping("/totalAlarm")
    @Operation(summary = "查询告警总数,未处理告警，已处理告警", description = "查询所有告警的总数")
    public Result<Map<String,Long>> totalAlarm() {
        Long total = alarmService.count();
        Long unhand = alarmService.countUnhand();
        Long hand = alarmService.countHand();
        Map araml=new HashMap();
        araml.put("告警总数",total);
        araml.put("未处理告警",unhand);
        araml.put("已处理告警",hand);
        return Result.success(araml);
    }
    @GetMapping("/page")
    @Operation(summary = "告警分页列表", description = "支持按设备名称、设备类别、状态筛选")
    public Result<Page<AlarmDetailVO>> getAlarmPage(AlarmQueryDTO dto) {
        return Result.success(alarmService.getAlarmPage(dto));
    }
}