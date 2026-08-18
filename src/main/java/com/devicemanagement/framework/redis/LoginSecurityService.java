package com.devicemanagement.framework.redis;

import com.devicemanagement.module.auth.dto.CaptchaResponse;
import com.devicemanagement.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.UUID;

/** 验证码、登录失败次数和短时锁定均放在 Redis，应用重启后不影响有效期。 */
@Service
@RequiredArgsConstructor
public class LoginSecurityService {

    private static final Duration CAPTCHA_TTL = Duration.ofMinutes(5);
    private static final Duration FAIL_TTL = Duration.ofMinutes(10);
    private static final int MAX_FAIL_COUNT = 5;

    private final StringRedisTemplate stringRedisTemplate;
    private final SecureRandom secureRandom = new SecureRandom();

    /** 生成并缓存一组五分钟有效的教学验证码。 */
    public CaptchaResponse createCaptcha() {
        String captchaId = UUID.randomUUID().toString();
        String captchaCode = String.valueOf(1000 + secureRandom.nextInt(9000));
        stringRedisTemplate.opsForValue().set(captchaKey(captchaId), captchaCode, CAPTCHA_TTL);
        return new CaptchaResponse(captchaId, captchaCode);
    }

    /** 校验并消费验证码，确保验证码仅能提交一次。 */
    public void verifyCaptcha(String captchaId, String captchaCode) {
        // 使用 Lua 脚本原子读取并删除，兼容 Redis 6.2 以下版本。
        String expectedCode = RedisStringValueConsumer.getAndDelete(stringRedisTemplate, captchaKey(captchaId));
        if (expectedCode == null || !expectedCode.equalsIgnoreCase(captchaCode)) {
            throw new BusinessException("验证码错误或已过期");
        }
    }

    /** 检查账号是否仍处于登录失败锁定期。 */
    public void checkLocked(String username) {
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(lockKey(username)))) {
            throw new BusinessException("登录失败次数过多，请10分钟后再试");
        }
    }

    /** 记录一次密码认证失败，达到阈值时锁定账号。 */
    public void onLoginFailure(String username) {
        Long count = stringRedisTemplate.opsForValue().increment(failKey(username));
        if (count != null && count == 1) {
            stringRedisTemplate.expire(failKey(username), FAIL_TTL);
        }
        if (count != null && count >= MAX_FAIL_COUNT) {
            stringRedisTemplate.opsForValue().set(lockKey(username), "1", FAIL_TTL);
        }
    }

    /** 密码认证成功后清除该账号的失败次数。 */
    public void onLoginSuccess(String username) {
        stringRedisTemplate.delete(failKey(username));
    }

    /** 拼接验证码的 Redis Key。 */
    private String captchaKey(String captchaId) {
        return "auth:captcha:" + captchaId;
    }

    /** 拼接登录失败计数的 Redis Key。 */
    private String failKey(String username) {
        return "auth:login:fail:" + username;
    }

    /** 拼接登录锁定标记的 Redis Key。 */
    private String lockKey(String username) {
        return "auth:login:lock:" + username;
    }
}
