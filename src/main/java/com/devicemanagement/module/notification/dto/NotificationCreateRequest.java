package com.devicemanagement.module.notification.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Schema(description = "创建通知请求")
public class NotificationCreateRequest {

    @NotNull(message = "接收用户ID不能为空")
    @Schema(description = "接收用户ID", example = "1")
    private Long userId;

    @Schema(description = "发起人ID（系统通知为null）", example = "1")
    private Long senderId;

    @NotBlank(message = "通知标题不能为空")
    @Schema(description = "通知标题", example = "新工单待处理")
    private String title;

    @NotBlank(message = "通知内容不能为空")
    @Schema(description = "通知内容", example = "您有新的工单")
    private String content;
}
