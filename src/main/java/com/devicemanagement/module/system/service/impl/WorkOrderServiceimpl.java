package com.devicemanagement.module.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.devicemanagement.module.system.entity.WorkOrder;
import com.devicemanagement.module.system.mapper.WorkOrderMapper;
import com.devicemanagement.module.system.service.WorkOrderService;
import com.devicemanagement.module.system.vo.WorkOrderDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class WorkOrderServiceimpl extends ServiceImpl<WorkOrderMapper, WorkOrder> implements WorkOrderService {
    @Autowired
    WorkOrderMapper workOrderMapper;

    @Override
    public WorkOrderDetailVO selectWorkOrderById(Long id) {
        System.out.println(2222);
        WorkOrderDetailVO workOrderDetailVO = workOrderMapper.selectWorkOrderById(id);
        System.out.println(3333);
        return workOrderDetailVO;
    }
}
