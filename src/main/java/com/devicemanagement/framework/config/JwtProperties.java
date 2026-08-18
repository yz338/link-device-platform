package com.devicemanagement.framework.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/** JWT 的有效期与密钥配置，统一从 application.yml 读取。 */
@Data
@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {

    private String secret;

    /** Access Token 有效期，单位为秒。 */
    private long expireSeconds;

    /** Refresh Token 有效期，单位为秒。 */
    private long refreshExpireSeconds;
}
