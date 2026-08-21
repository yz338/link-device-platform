package com.devicemanagement.module.notification.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.devicemanagement.module.notification.dto.NotificationCreateRequest;
import com.devicemanagement.module.notification.entity.Notification;
import com.devicemanagement.module.notification.vo.NotificationVO;
import com.devicemanagement.module.notification.vo.UnreadCountVO;

public interface NotificationService extends IService<Notification> {

    /** 创建通知 */
    void createNotification(NotificationCreateRequest request);

    /** 自动创建通知（供业务模块内部调用） */
    void createNotification(Long userId, Long senderId, String title, String content);

    /** 分页查询用户通知 */
    Page<NotificationVO> pageByUserId(Long userId, int pageNum, int pageSize);

    /** 获取未读通知数量 */
    UnreadCountVO countUnread(Long userId);

    /** 标记单条已读 */
    void markAsRead(Long id);

    /** 全部标记已读 */
    void markAllAsRead(Long userId);

    /** 删除通知 */
    void deleteNotification(Long id);
}
