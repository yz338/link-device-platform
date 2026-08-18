package com.devicemanagement.module.workorder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.devicemanagement.module.workorder.bean.WorkOrder;
import com.devicemanagement.module.workorder.vo.WorkOrderDetailVO;


public interface WorkOrderService extends IService<WorkOrder> {
    public WorkOrderDetailVO selectWorkOrderById(Long id);
}
