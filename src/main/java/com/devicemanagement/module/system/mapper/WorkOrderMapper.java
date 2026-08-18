package com.devicemanagement.module.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.devicemanagement.module.system.entity.WorkOrder;
import com.devicemanagement.module.system.vo.WorkOrderDetailVO;
import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface WorkOrderMapper extends BaseMapper<WorkOrder> {
    public WorkOrderDetailVO selectWorkOrderById(Long id);
}
