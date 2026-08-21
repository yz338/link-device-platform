package com.devicemanagement.module.notification.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "未读通知数量VO")
public class UnreadCountVO {

    @Schema(description = "未读通知数量", example = "5")
    private Long unreadCount;

    public UnreadCountVO(Long unreadCount) {
        this.unreadCount = unreadCount;
    }
}
