package com.devicemanagement.framework.config;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 全局统一 LocalDateTime 的 JSON 格式。
 * 说明：application.yaml 的 spring.jackson.date-format 只作用于 java.util.Date，
 * 对 LocalDateTime 必须在此注册 JavaTimeModule 的自定义序列化/反序列化器。
 */
@Configuration
public class JacksonConfig {

    public static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer localDateTimeFormatCustomizer() {
        return builder -> builder
                // 响应：LocalDateTime -> "2026-08-20 10:53:00"
                .serializers(new LocalDateTimeSerializer(DATE_TIME_FORMATTER))
                // 请求：JSON 里的 "2026-08-25 18:00:00" -> LocalDateTime
                .deserializers(new LocalDateTimeDeserializer(DATE_TIME_FORMATTER));
    }
}
