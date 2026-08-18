package com.devicemanagement.module.system.controller;

import com.devicemanagement.common.Result;
import com.devicemanagement.module.system.entity.WorkOrder;
import com.devicemanagement.module.system.service.WorkOrderService;
import com.devicemanagement.module.system.vo.WorkOrderDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workOrder")
public class WorkOrderController {
    @Autowired
    private WorkOrderService workOrderService;
    @GetMapping("/orderInfo")
    public Result<List<WorkOrder>> getWorkOrderAll(){
        System.out.println(1111);
        List<WorkOrder> list = workOrderService.list();
        System.out.println(4444);
        return Result.success(list);
    }
    @GetMapping("/orderDetail")
    public Result<WorkOrderDetailVO> orderDetail(@RequestParam("id") Long id){
        System.out.println(5555);
        WorkOrderDetailVO workOrderDetailVO = workOrderService.selectWorkOrderById(id);
        System.out.println(6666);
        return Result.success(workOrderDetailVO);
    }
}
