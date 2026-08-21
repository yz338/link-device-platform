package com.devicemanagement.module.notification.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.devicemanagement.common.exception.BusinessException;
import com.devicemanagement.framework.redis.NotificationUnreadCacheService;
import com.devicemanagement.module.notification.dto.NotificationCreateRequest;
import com.devicemanagement.module.notification.entity.Notification;
import com.devicemanagement.module.notification.mapper.NotificationMapper;
import com.devicemanagement.module.notification.service.NotificationService;
import com.devicemanagement.module.notification.vo.NotificationVO;
import com.devicemanagement.module.notification.vo.UnreadCountVO;
import com.devicemanagement.module.system.entity.SysUser;
import com.devicemanagement.module.system.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private NotificationUnreadCacheService unreadCacheService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createNotification(NotificationCreateRequest request) {
        if (request.getSenderId() != null && request.getSenderId().equals(request.getUserId())) {
            throw new BusinessException("不能给自己发送通知");
        }
        Notification notification = new Notification();
        notification.setUserId(request.getUserId());
        notification.setSenderId(request.getSenderId());
        notification.setTitle(request.getTitle());
        notification.setContent(request.getContent());
        notification.setIsRead(0);
        notification.setCreateTime(new Date());
        notificationMapper.insert(notification);
        unreadCacheService.incrementAfterCommit(request.getUserId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createNotification(Long userId, Long senderId, String title, String content) {
        if (senderId != null && senderId.equals(userId)) {
            return;
        }
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setSenderId(senderId);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setIsRead(0);
        notification.setCreateTime(new Date());
        notificationMapper.insert(notification);
        unreadCacheService.incrementAfterCommit(userId);
    }

    @Override
    public Page<NotificationVO> pageByUserId(Long userId, int pageNum, int pageSize) {
        Page<Notification> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Notification> wrapper = Wrappers.<Notification>lambdaQuery()
                .eq(Notification::getUserId, userId)
                .orderByDesc(Notification::getCreateTime);
        Page<Notification> notificationPage = notificationMapper.selectPage(page, wrapper);

        List<NotificationVO> voList = notificationPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        Page<NotificationVO> result = new Page<>(notificationPage.getCurrent(), notificationPage.getSize(), notificationPage.getTotal());
        result.setRecords(voList);
        return result;
    }

    @Override
    public UnreadCountVO countUnread(Long userId) {
        // 走 Redis 缓存，未命中时自动查 DB 回填
        long count = unreadCacheService.getUnreadCount(userId);
        return new UnreadCountVO(count);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markAsRead(Long id) {
        // 先查通知，拿到 userId 用来更新缓存
        Notification notification = notificationMapper.selectById(id);
        if (notification == null) return;
        notificationMapper.markAsRead(id);
        if (notification.getIsRead() != null && notification.getIsRead() == 0) {
            unreadCacheService.decrementAfterCommit(notification.getUserId());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markAllAsRead(Long userId) {
        notificationMapper.markAllAsRead(userId);
        unreadCacheService.invalidateAfterCommit(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteNotification(Long id) {
        Notification notification = notificationMapper.selectById(id);
        if (notification == null) return;
        notificationMapper.deleteById(id);
        // 如果删除的是一条未读通知，需要更新未读缓存
        if (notification.getIsRead() != null && notification.getIsRead() == 0) {
            unreadCacheService.decrementAfterCommit(notification.getUserId());
        }
    }

    private NotificationVO convertToVO(Notification notification) {
        NotificationVO vo = new NotificationVO();
        vo.setId(notification.getId());
        vo.setUserId(notification.getUserId());
        vo.setSenderId(notification.getSenderId());
        vo.setTitle(notification.getTitle());
        vo.setContent(notification.getContent());
        vo.setIsRead(notification.getIsRead());
        vo.setReadTime(notification.getReadTime());
        vo.setCreateTime(notification.getCreateTime());

        if (notification.getSenderId() != null) {
            SysUser sender = sysUserMapper.selectById(notification.getSenderId());
            if (sender != null) {
                vo.setSenderName(sender.getNickname());
            }
        }
        return vo;
    }
}
