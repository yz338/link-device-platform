package com.devicemanagement.framework.redis;

import com.devicemanagement.module.notification.mapper.NotificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.Duration;

/**
 * 缓存用户未读通知数量。
 * 前端每 60 秒轮询未读数 + 页面可见性变化都会查询，用 Redis 挡住大部分数据库压力。
 */
@Service
@RequiredArgsConstructor
public class NotificationUnreadCacheService {

    private static final Duration CACHE_TTL = Duration.ofMinutes(5);

    private final StringRedisTemplate stringRedisTemplate;
    private final NotificationMapper notificationMapper;

    /**
     * 优先从 Redis 读取用户未读通知数量，未命中时查 DB 并回填。
     */
    public long getUnreadCount(Long userId) {
        String key = key(userId);
        String cachedValue = stringRedisTemplate.opsForValue().get(key);
        if (cachedValue != null) {
            try {
                return Long.parseLong(cachedValue);
            } catch (NumberFormatException ignored) {
                // 脏数据，走 DB 回填
            }
        }
        long count = notificationMapper.countUnreadByUserId(userId);
        stringRedisTemplate.opsForValue().set(key, String.valueOf(count), CACHE_TTL);
        return count;
    }

    /**
     * 产生一条新通知时调用：未读数 +1
     * 仅当 key 已存在时递增（避免脏写），不存在则直接删除让下一次 get 回源
     */
    public void incrementAfterCommit(Long userId) {
        afterCommit(() -> {
            String key = key(userId);
            Boolean hasKey = stringRedisTemplate.hasKey(key);
            if (Boolean.TRUE.equals(hasKey)) {
                stringRedisTemplate.opsForValue().increment(key);
            }
        });
    }

    /**
     * 单条通知标为已读时调用：未读数 -1（最小为 0）
     */
    public void decrementAfterCommit(Long userId) {
        afterCommit(() -> {
            String key = key(userId);
            Boolean hasKey = stringRedisTemplate.hasKey(key);
            if (!Boolean.TRUE.equals(hasKey)) return;
            Long v = stringRedisTemplate.opsForValue().decrement(key);
            if (v != null && v < 0) {
                stringRedisTemplate.opsForValue().set(key, "0", CACHE_TTL);
            }
        });
    }

    /**
     * 删除缓存，下次 getUnreadCount 重新从 DB 加载（用于全部已读等批量操作）
     */
    public void invalidateAfterCommit(Long userId) {
        afterCommit(() -> stringRedisTemplate.delete(key(userId)));
    }

    /**
     * 事务提交后执行 Redis 操作，避免写操作早于 DB 提交导致缓存和 DB 不一致。
     */
    private void afterCommit(Runnable action) {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            action.run();
            return;
        }
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                action.run();
            }
        });
    }

    private String key(Long userId) {
        return "notification:unread:" + userId;
    }
}
