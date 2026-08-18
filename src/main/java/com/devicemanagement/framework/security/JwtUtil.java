package com.devicemanagement.framework.security;

import com.devicemanagement.framework.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

/** JWT 的签发与解析只集中在本类，避免散落在 Controller 或过滤器中。 */
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtProperties jwtProperties;

    /** 为指定用户名生成短期 Access Token。 */
    public String generateAccessToken(String username) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(username)
                // jti 是 Token 黑名单的短 Key，不直接把完整 JWT 写入 Redis。
                .id(UUID.randomUUID().toString())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(jwtProperties.getExpireSeconds())))
                .signWith(signingKey())
                .compact();
    }

    /** 解析并校验 JWT 的签名和有效期。 */
    public Claims parseToken(String token) {
        return Jwts.parser().verifyWith(signingKey()).build()
                .parseSignedClaims(token).getPayload();
    }

    /** 根据配置密钥构造 HMAC 签名密钥。 */
    private SecretKey signingKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }
}
