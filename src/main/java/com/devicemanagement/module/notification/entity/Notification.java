package com.devicemanagement.module.notification.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@TableName("notification")
@Schema(description = "通知消息")
public class Notification {

    @Schema(description = "主键ID", example = "1")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "接收用户ID", example = "1")
    private Long userId;

    @Schema(description = "发起人ID（系统通知为null）", example = "1")
    private Long senderId;

    @Schema(description = "通知标题", example = "新工单待处理")
    private String title;

    @Schema(description = "通知内容", example = "您有新的工单：xx设备报警")
    private String content;

    @Schema(description = "是否已读：0-未读 1-已读", example = "0")
    private Integer isRead;

    @Schema(description = "阅读时间", example = "2026-08-20 10:53:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date readTime;

    @Schema(description = "创建时间", example = "2026-08-20 10:53:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
