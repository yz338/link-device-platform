package com.devicemanagement.module.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.devicemanagement.module.system.entity.WorkOrder;
import com.devicemanagement.module.system.vo.WorkOrderDetailVO;


public interface WorkOrderService extends IService<WorkOrder> {
    public WorkOrderDetailVO selectWorkOrderById(Long id);
}
