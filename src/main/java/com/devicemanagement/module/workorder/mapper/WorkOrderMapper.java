package com.devicemanagement.module.workorder.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.devicemanagement.module.workorder.bean.WorkOrder;
import com.devicemanagement.module.workorder.vo.WorkOrderDetailVO;
import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface WorkOrderMapper extends BaseMapper<WorkOrder> {
    public WorkOrderDetailVO selectWorkOrderById(Long id);
}
