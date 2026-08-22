package com.devicemanagement.module.notification.service;

import com.devicemanagement.module.system.entity.SysUser;
import com.devicemanagement.module.system.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * 站内通知同步发送到真实邮箱。
 *
 * <p>原则：邮件发送失败<strong>绝不影响站内信写入</strong>，因此所有异常都在此类内部吞掉并打 warn 日志。
 * 以下情况直接跳过不发邮件：
 * <ul>
 *     <li>application.yml 未配置 spring.mail.host（JavaMailSender bean 不存在）</li>
 *     <li>目标用户 sys_user.email 为空或格式不合法</li>
 *     <li>spring.mail.username 发件人未配置</li>
 * </ul>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailNotificationService {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /** Spring Boot 2.7 + starter-mail 自动装配，没配 SMTP 时此 bean 不会被注入。 */
    private final JavaMailSender javaMailSender;
    private final SysUserMapper sysUserMapper;

    @Value("${spring.mail.username:}")
    private String fromEmail;
    @Value("${spring.application.name:设备运维系统}")
    private String appName;

    /**
     * 异步尝试将一条通知同步发送到指定用户的真实邮箱。
     * 任何错误都会被吞掉，仅记日志。
     */
    @Async
    public void trySendToUser(Long userId, String title, String content) {
        if (userId == null) return;
        if (!isMailReady()) {
            log.debug("[邮件同步] SMTP 未配置（spring.mail.host / JavaMailSender 缺失），跳过发送");
            return;
        }
        SysUser user;
        try {
            user = sysUserMapper.selectById(userId);
        } catch (Exception e) {
            log.warn("[邮件同步] 查询用户 userId={} 失败，跳过邮件", userId, e);
            return;
        }
        if (user == null) return;
        String to = user.getEmail();
        if (isBlank(to)) {
            log.debug("[邮件同步] 用户 userId={} 未填写邮箱，跳过发送（nickname={}）", userId, user.getNickname());
            return;
        }
        if (!EMAIL_PATTERN.matcher(to.trim()).matches()) {
            log.warn("[邮件同步] 用户 userId={} 邮箱格式不合法：{}，跳过发送", userId, to);
            return;
        }
        sendInternal(to, user.getNickname(), title, content);
    }

    // =================== internal ===================

    private boolean isMailReady() {
        return javaMailSender != null
                && !isBlank(fromEmail);
    }

    private static boolean isBlank(String s) {
        if (s == null) return true;
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isWhitespace(s.charAt(i))) return false;
        }
        return true;
    }

    private void sendInternal(String to, String nickname, String title, String content) {
        try {
            MimeMessage msg = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");
            helper.setFrom(fromEmail, appName);
            helper.setTo(to.trim());
            helper.setSubject("【" + appName + "】" + defaultString(title, "新通知"));
            String html = renderHtml(nickname, title, content, new Date());
            helper.setText(html, true);
            javaMailSender.send(msg);
            log.info("[邮件同步] 已发送 通知标题={} 收件人={}", title, to);
        } catch (Exception e) {
            // 任何异常都不抛给上层，避免回滚站内信
            log.warn("[邮件同步] 发送失败 收件人={} 标题={} cause={}", to, title, e.toString());
            if (log.isDebugEnabled()) log.debug("  详细堆栈：", e);
        }
    }

    private String renderHtml(String nickname, String title, String content, Date createTime) {
        String safeTitle = escape(defaultString(title, "新通知"));
        String safeContent = escape(defaultString(content, "")).replace("\n", "<br/>");
        String safeNick = escape(defaultString(nickname, "用户"));
        String time = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime);
        return "<!doctype html><html><head><meta charset='utf-8'/></head><body style='margin:0;padding:0;background:#f5f7fa;font-family:Helvetica,Arial,sans-serif;'>"
                + "<table width='640' cellpadding='0' cellspacing='0' border='0' align='center' style='margin:24px auto;background:#fff;border-radius:8px;overflow:hidden;border:1px solid #eaecef;'>"
                + "<tr><td style='padding:18px 28px;background:#1e88e5;color:#fff;font-size:16px;font-weight:600;letter-spacing:1px;'>" + appName + " · 站内通知同步</td></tr>"
                + "<tr><td style='padding:24px 28px;'>"
                + "<p style='margin:0 0 8px;color:#303133;font-size:14px;'>尊敬的 <b>" + safeNick + "</b> 您好：</p>"
                + "<p style='margin:0 0 16px;color:#606266;font-size:13px;line-height:1.6;'>您在系统中有一条新的站内通知，详情如下：</p>"
                + "<div style='background:#f5f7fa;border-left:4px solid #1e88e5;padding:12px 16px;border-radius:4px;'>"
                + "<div style='margin:0 0 6px;font-size:13px;color:#909399;'>通知标题</div>"
                + "<div style='margin:0 0 12px;font-size:15px;font-weight:600;color:#303133;'>" + safeTitle + "</div>"
                + "<div style='margin:0 0 6px;font-size:13px;color:#909399;'>通知内容</div>"
                + "<div style='margin:0 0 8px;font-size:14px;line-height:1.7;color:#303133;'>" + safeContent + "</div>"
                + "</div>"
                + "<p style='margin:20px 0 0;color:#909399;font-size:12px;line-height:1.6;'>"
                + "通知生成时间：" + time + "<br/>"
                + "本邮件由系统自动发送，请勿直接回复。如需处理请登录系统查看完整通知与操作按钮。"
                + "</p>"
                + "</td></tr>"
                + "<tr><td style='padding:12px 28px;background:#fafbfc;color:#c0c4cc;font-size:12px;text-align:center;border-top:1px solid #eaecef;'>"
                + "&copy; " + java.time.Year.now().getValue() + " " + appName + "</td></tr>"
                + "</table></body></html>";
    }

    private static String defaultString(String s, String fallback) {
        return (s == null || s.isEmpty()) ? fallback : s;
    }

    private static String escape(String s) {
        if (s == null) return "";
        StringBuilder sb = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '<': sb.append("&lt;"); break;
                case '>': sb.append("&gt;"); break;
                case '&': sb.append("&amp;"); break;
                case '"': sb.append("&quot;"); break;
                case '\'': sb.append("&#39;"); break;
                default: sb.append(c);
            }
        }
        return sb.toString();
    }
}
