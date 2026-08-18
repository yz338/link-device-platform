package com.devicemanagement.framework.redis;

import com.devicemanagement.module.system.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * 缓存用户最终拥有的角色和权限码。
 * Spring Security、profile 接口和前端按钮权限共用这份数据，避免三处查询结果不一致。
 */
@Service
@RequiredArgsConstructor
public class UserAuthorityCacheService {

    private static final Duration CACHE_TTL = Duration.ofMinutes(30);

    private final StringRedisTemplate stringRedisTemplate;
    private final SysUserMapper sysUserMapper;

    /** 优先从 Redis 读取用户最终拥有的角色与权限码，未命中时查询数据库并回填。 */
    public List<String> getAuthorityCodes(Long userId) {
        String cachedValue = stringRedisTemplate.opsForValue().get(key(userId));
        if (cachedValue != null) {
            return cachedValue.isBlank() ? List.of() : List.of(cachedValue.split(","));
        }

        List<String> authorityCodes = new ArrayList<>();
        sysUserMapper.selectRoleCodesByUserId(userId)
                .forEach(roleCode -> authorityCodes.add("ROLE_" + roleCode));
        authorityCodes.addAll(sysUserMapper.selectPermissionCodesByUserId(userId));
        stringRedisTemplate.opsForValue().set(key(userId), String.join(",", authorityCodes), CACHE_TTL);
        return authorityCodes;
    }

    /** 删除用户权限缓存，使下一次请求重新加载权限。 */
    public void invalidate(Long userId) {
        stringRedisTemplate.delete(key(userId));
    }

    /**
     * 在当前事务提交后删除权限缓存。
     * 这样并发请求不会在数据库事务尚未提交时读取旧权限并重新写入缓存。
     */
    public void invalidateAfterCommit(Long userId) {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            invalidate(userId);
            return;
        }
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                invalidate(userId);
            }
        });
    }

    /** 拼接用户权限缓存的 Redis Key。 */
    private String key(Long userId) {
        return "auth:permission:" + userId;
    }
}
