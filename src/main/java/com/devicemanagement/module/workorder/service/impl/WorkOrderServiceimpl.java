package com.devicemanagement.module.workorder.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.devicemanagement.module.workorder.bean.WorkOrder;
import com.devicemanagement.module.workorder.mapper.WorkOrderMapper;
import com.devicemanagement.module.workorder.service.WorkOrderService;
import com.devicemanagement.module.workorder.vo.WorkOrderDetailVO;
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
