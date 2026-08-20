package com.devicemanagement.module.device.controller;

import com.devicemanagement.common.Result;
import com.devicemanagement.module.device.dto.DeviceAttrUpdateDTO;
import com.devicemanagement.module.device.dto.DeviceDTO;
import com.devicemanagement.module.device.entity.Device;
import com.devicemanagement.module.device.service.DeviceService;
import com.devicemanagement.module.device.vo.DeviceDetailVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/device")
@Tag(name = "设备管理", description = "设备的增删改查、导入、详情等接口")
public class DeviceController {
    @Autowired
    private DeviceService deviceService;

    @GetMapping("/list")
    @Operation(summary = "查询设备列表", description = "支持按设备名称、位置、状态、类别等条件筛选")
    public Result<List<Device>> deviceList(DeviceDTO deviceDTO) {
        return Result.success(deviceService.deviceList(deviceDTO));
    }

    @PostMapping("/addDevice")
    @Operation(summary = "新增设备", description = "新增设备时会自动根据设备类别创建对应的属性记录")
    public Result<String> addDevice(@RequestBody DeviceDTO deviceDTO) {
        deviceService.save(deviceDTO);
        return Result.success("添加成功");
    }

    @PutMapping("/updateDevice")
    @Operation(summary = "修改设备信息", description = "修改设备基本信息，不修改属性")
    public Result<Device> updateDevice(@RequestBody DeviceDTO deviceDTO) {
        Device device = deviceService.updateDevice(deviceDTO);
        return Result.success(device);
    }

    @DeleteMapping("/deleteDevice")
    @Operation(summary = "删除设备", description = "根据设备ID删除设备及其关联的属性记录")
    public Result<String> deleteDevice(
            @Parameter(description = "设备ID", required = true) @RequestParam Long id) {
        String result = deviceService.deleteDeviceById(id);
        return Result.success(result);
    }

    @GetMapping("/queryCategoryName")
    @Operation(summary = "查询所有设备类别名称", description = "返回所有启用状态的设备类别名称列表")
    public Result<List<String>> queryCategoryName() {
        List<String> CategoryName = deviceService.queryCategoryName(null);
        return Result.success(CategoryName);
    }

    @GetMapping("/queryCategoryIdByCategoryName")
    @Operation(summary = "按类别查询设备", description = "根据类别名称查询该类别下的所有设备")
    public Result<List<Device>> queryByCategory(
            @Parameter(description = "类别名称", required = true) @RequestParam String categoryName) {
        List<Device> devices = deviceService.queryByCategory(categoryName);
        return Result.success(devices);
    }

    @PostMapping("/importExcel")
    @Operation(summary = "Excel批量导入设备", description = "上传Excel文件批量导入设备，格式需与模板一致")
    public Result<String> importExcel(
            @Parameter(description = "Excel文件", required = true) @RequestParam("file") MultipartFile file) throws Exception {
        deviceService.importExcel(file);
        return Result.success("导入成功");
    }

    @GetMapping("/Detail")
    @Operation(summary = "查询设备详情", description = "根据设备ID查询设备详细信息，包含基本信息、属性值和最新报送时间")
    public Result<DeviceDetailVO> deviceDetail(
            @Parameter(description = "设备ID", required = true) @RequestParam Long id) {
        DeviceDetailVO deviceDetail = deviceService.getDeviceDetail(id);
        return Result.success(deviceDetail);
    }

    @PutMapping("/updateAttr")
    @Operation(summary = "修改设备属性", description = "根据设备ID修改对应的设备属性值，未传的字段保持原值")
    public Result<String> updateDeviceAttr(@RequestBody DeviceAttrUpdateDTO dto) {
        deviceService.updateDeviceAttr(dto);
        return Result.success("属性更新成功");
    }
}
