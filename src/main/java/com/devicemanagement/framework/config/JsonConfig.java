package com.devicemanagement.framework.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Boot 4 通过 spring-boot-starter-jackson 自动注册 MVC JSON 转换器。
 * 本类只提供给 Spring Security 异常处理器使用的 ObjectMapper，避免使用已弃用的手动转换器注册 API。
 */
@Configuration
public class JsonConfig {

    /** 创建供异常处理器与 MVC 共用的 JSON 序列化器。 */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().findAndRegisterModules();
    }

}
