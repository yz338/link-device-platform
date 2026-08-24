package com.devicemanagement.module.workorder.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.devicemanagement.module.workorder.bean.WorkOrder;
import com.devicemanagement.module.workorder.dto.CreateWorkOrderRequest;
import com.devicemanagement.module.workorder.dto.VerifyWorkOrderRequest;
import com.devicemanagement.module.workorder.dto.WorkOrderQueryDTO;
import com.devicemanagement.module.workorder.vo.WorkOrderStatsVO;
import com.devicemanagement.module.workorder.vo.WorkOrderVO;
import org.springframework.web.multipart.MultipartFile;


public interface WorkOrderService extends IService<WorkOrder> {
      /** 下发工单 */
      void createWorkOrder(CreateWorkOrderRequest request);
      /** 更改处理人 */
      void updateHandlerUser(Long userId, Long orderId);
      /** 分页查询工单（带设备名称等冗余字段） */
      Page<WorkOrderVO> pageList(WorkOrderQueryDTO dto);
      /** 工单详情 */
      WorkOrderVO getDetail(Long orderId);
      /** 开始处理：工单与告警进入处理中 */
      void startProcess(Long orderId);
      /** 处理工单：处理人提交处理记录） */
      void processWorkOrder(Long orderId, String method, String processDescription, MultipartFile[] images);
      /** 核验工单：管理员提交核验记录 */
      void verifyWorkOrder(VerifyWorkOrderRequest request);
      /** 工单统计 */
      WorkOrderStatsVO getStats();
}
