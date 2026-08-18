package com.devicemanagement.framework.redis;

import com.devicemanagement.common.exception.BusinessException;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

/** 退出登录后将 Access Token 的 jti 放入黑名单，直到原 Token 自然过期。 */
@Service
@RequiredArgsConstructor
public class TokenBlacklistService {

    private final StringRedisTemplate stringRedisTemplate;

    /** 将仍在有效期内的 Access Token 标识加入黑名单。 */
    public void add(Claims claims) {
        long seconds = claims.getExpiration().toInstant().getEpochSecond() - Instant.now().getEpochSecond();
        if (claims.getId() == null || seconds <= 0) {
            throw new BusinessException("Access Token无效或已过期");
        }
        stringRedisTemplate.opsForValue().set(key(claims.getId()), "1", Duration.ofSeconds(seconds));
    }

    /** 判断 JWT 的唯一标识是否已经进入黑名单。 */
    public boolean isBlacklisted(String jti) {
        return jti != null && Boolean.TRUE.equals(stringRedisTemplate.hasKey(key(jti)));
    }

    /** 拼接黑名单 Redis Key。 */
    private String key(String jti) {
        return "auth:token:blacklist:" + jti;
    }
}
