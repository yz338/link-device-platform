package com.devicemanagement.module.workorder.vo;

import lombok.Data;

import java.util.Date;

@Data
public class WorkOrderDetailVO {
    private Integer orderId;
    private String workOrderId;
    private String orderTitle;
    private String relateDevice;
    private String workDesc;
    private String alarmContent;
    private Date deathline;
    private String orderStatus;
    private String handleMethod;
    private String handleExplain;
    private String verifyStatus;
    private String verifyDecision;
    private String verifyOpinion;
    private String attachment;
}