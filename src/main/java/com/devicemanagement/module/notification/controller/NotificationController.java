package com.devicemanagement.module.notification.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.devicemanagement.common.Result;
import com.devicemanagement.module.notification.dto.NotificationCreateRequest;
import com.devicemanagement.module.notification.service.NotificationService;
import com.devicemanagement.module.notification.vo.NotificationVO;
import com.devicemanagement.module.notification.vo.UnreadCountVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/notification")
@Tag(name = "通知管理", description = "通知消息的查询、标记已读、删除等操作")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    /** 分页查询当前用户通知 */
    @GetMapping("/list")
    @Operation(summary = "分页查询通知")
    public Result<Page<NotificationVO>> list(
            @Parameter(description = "用户ID") @RequestParam Long userId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int pageNum,
            @Parameter(description = "每页条数") @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(notificationService.pageByUserId(userId, pageNum, pageSize));
    }

    /** 获取未读通知数量 */
    @GetMapping("/unread-count")
    @Operation(summary = "获取未读通知数量")
    public Result<UnreadCountVO> unreadCount(@Parameter(description = "用户ID") @RequestParam Long userId) {
        return Result.success(notificationService.countUnread(userId));
    }

    /** 标记单条通知为已读 */
    @PutMapping("/read/{id}")
    @Operation(summary = "标记已读")
    public Result<String> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return Result.success("标记已读成功");
    }

    /** 全部标记为已读 */
    @PutMapping("/read-all")
    @Operation(summary = "全部标记已读")
    public Result<String> markAllAsRead(@Parameter(description = "用户ID") @RequestParam Long userId) {
        notificationService.markAllAsRead(userId);
        return Result.success("全部标记已读成功");
    }

    /** 删除通知 */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除通知")
    public Result<String> delete(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return Result.success("删除成功");
    }

    /** 创建通知（管理员用） */
    @PostMapping("/create")
    @Operation(summary = "创建通知")
    public Result<String> create(@Valid @RequestBody NotificationCreateRequest request) {
        notificationService.createNotification(request);
        return Result.success("创建通知成功");
    }
}
