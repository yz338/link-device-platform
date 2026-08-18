package com.devicemanagement.framework.redis;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.List;

/**
 * 兼容旧版 Redis 的字符串读取并删除工具。
 * Redis 6.2 以下没有 GETDEL 命令，使用 Lua 脚本仍可保证读取与删除不可分割。
 */
public final class RedisStringValueConsumer {

    private static final DefaultRedisScript<String> GET_AND_DELETE_SCRIPT = new DefaultRedisScript<>("""
            local value = redis.call('GET', KEYS[1])
            if value then
                redis.call('DEL', KEYS[1])
            end
            return value
            """, String.class);

    /** 工具类不允许创建实例。 */
    private RedisStringValueConsumer() {
    }

    /**
     * 原子读取指定 Key 的字符串值，并在读取到值后立即删除该 Key。
     *
     * @param stringRedisTemplate 字符串 Redis 操作对象
     * @param key                 待读取并删除的 Key
     * @return 原值；Key 不存在或已过期时返回 {@code null}
     */
    public static String getAndDelete(StringRedisTemplate stringRedisTemplate, String key) {
        return stringRedisTemplate.execute(GET_AND_DELETE_SCRIPT, List.of(key));
    }
}
