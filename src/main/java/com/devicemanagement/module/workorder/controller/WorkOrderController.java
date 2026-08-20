package com.devicemanagement.module.workorder.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.devicemanagement.common.Result;
import com.devicemanagement.module.workorder.bean.WorkOrder;
import com.devicemanagement.module.workorder.dto.CreateWorkOrderRequest;
import com.devicemanagement.module.workorder.dto.VerifyWorkOrderRequest;
import com.devicemanagement.module.workorder.dto.WorkOrderQueryDTO;
import com.devicemanagement.module.workorder.service.WorkOrderService;
import com.devicemanagement.module.workorder.vo.WorkOrderVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/workOrder")
@Tag(name = "工单管理", description = "工单的下发、处理、核验等操作")
public class WorkOrderController {
    @Autowired
    private WorkOrderService workOrderService;

    /** 下发工单 */
    @PostMapping("/create")
    @Operation(summary = "下发工单")
    public Result<String> create(@Valid @RequestBody CreateWorkOrderRequest request) {
        workOrderService.createWorkOrder(request);
        return Result.success("下发工单成功");
    }

    /** 变更工单处理人 */
    @PostMapping("/changeHandler")
    @Operation(summary = "变更工单处理人")
    public Result<String> changeHandler(@RequestParam Long orderId, @RequestParam Long userId) {
        workOrderService.updateHandlerUser(userId, orderId);
        return Result.success("变更处理人成功");
    }

    /** 开始处理：工单与告警进入处理中 */
    @PostMapping("/startProcess")
    @Operation(summary = "开始处理工单")
    public Result<String> startProcess(@RequestParam Long orderId) {
        workOrderService.startProcess(orderId);
        return Result.success("已将工单状态与告警状态设置为开始处理");
    }

    /** 处理工单：处理人提交处理记录（multipart，最多 9 张图片），工单变为待核验 */
    @PostMapping(value = "/process", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "处理工单")
    public Result<String> process(@RequestParam Long orderId,
                                  @RequestParam String method,
                                  @RequestParam String processDescription,
                                  @RequestParam(value = "images", required = false) MultipartFile[] images) {
        workOrderService.processWorkOrder(orderId, method, processDescription, images);
        return Result.success("处理工单成功");
    }

    /** 核验工单：管理员提交核验记录 */
    @PostMapping("/verify")
    @Operation(summary = "核验工单")
    public Result<Void> verify(@Valid @RequestBody VerifyWorkOrderRequest request) {
        workOrderService.verifyWorkOrder(request);
        return Result.success(null);
    }

    /** 上传处理完成图片（支持多张） */
    @PostMapping("/uploadImages")
    @Operation(summary = "上传处理图片")
    public Result<List<String>> uploadImages(@RequestParam Long orderId,
                                             @RequestParam("images") MultipartFile[] images) {
        return Result.success(workOrderService.uploadImages(orderId, images));
    }

    /** 分页查询工单 */
    @GetMapping("/list")
    @Operation(summary = "分页查询工单")
    public Result<Page<WorkOrder>> list(WorkOrderQueryDTO dto) {
        return Result.success(workOrderService.pageList(dto));
    }

    /** 工单详情 */
    @GetMapping("/{orderId}")
    @Operation(summary = "工单详情")
    public Result<WorkOrderVO> detail(@PathVariable Long orderId) {
        return Result.success(workOrderService.getDetail(orderId));
    }

}
