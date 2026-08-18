package com.devicemanagement.framework.redis;


import com.devicemanagement.framework.config.JwtProperties;
import com.devicemanagement.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Base64;

/** Refresh Token 采用随机字符串并保存在 Redis，而不是再次使用 JWT。 */
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final StringRedisTemplate stringRedisTemplate;
    private final JwtProperties jwtProperties;
    private final SecureRandom secureRandom = new SecureRandom();

    /** 创建随机 Refresh Token 并以用户名作为 Redis Value 保存。 */
    public String create(String username) {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        String refreshToken = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        stringRedisTemplate.opsForValue().set(key(refreshToken), username,
                Duration.ofSeconds(jwtProperties.getRefreshExpireSeconds()));
        return refreshToken;
    }

    /** 消费 Refresh Token，读取成功后立即使旧 Token 失效。 */
    public String consume(String refreshToken) {
        // Refresh Token 轮换：使用 Lua 脚本原子消费旧 Token，兼容 Redis 6.2 以下版本。
        String username = RedisStringValueConsumer.getAndDelete(stringRedisTemplate, key(refreshToken));
        if (username == null) {
            throw new BusinessException("Refresh Token无效或已过期");
        }
        return username;
    }

    /** 在退出登录时主动删除指定 Refresh Token。 */
    public void delete(String refreshToken) {
        if (refreshToken != null && !refreshToken.isBlank()) {
            stringRedisTemplate.delete(key(refreshToken));
        }
    }

    /** 拼接 Refresh Token 的 Redis Key。 */
    private String key(String refreshToken) {
        return "auth:refresh:" + refreshToken;
    }
}
