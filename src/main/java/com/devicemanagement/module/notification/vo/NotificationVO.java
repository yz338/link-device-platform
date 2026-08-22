package com.devicemanagement.module.notification.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "通知VO")
public class NotificationVO {

    @Schema(description = "通知ID", example = "1")
    private Long id;

    @Schema(description = "接收用户ID", example = "1")
    private Long userId;

    @Schema(description = "发起人ID（系统通知为null）", example = "1")
    private Long senderId;

    @Schema(description = "发起人昵称", example = "管理员")
    private String senderName;

    @Schema(description = "通知标题", example = "新工单待处理")
    private String title;

    @Schema(description = "通知内容", example = "您有新的工单")
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
