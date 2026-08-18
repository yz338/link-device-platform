package com.devicemanagement.module.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("work_order")
public class WorkOrder {
    @TableId(value = "order_id",type = IdType.AUTO)
    private Long orderId;
    private String orderTitle;
    private Long alarmId;
    private Long order_status;
    private Date createTime;
    private Date updateTime;
}
