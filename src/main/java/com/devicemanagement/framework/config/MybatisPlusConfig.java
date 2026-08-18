package com.devicemanagement.framework.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** 启用 MyBatis-Plus 的 MySQL 分页，用户列表可直接返回 Page 对象。 */
@Configuration
public class MybatisPlusConfig {

    /** 注册分页拦截器，使 Page 查询生成 MySQL 分页 SQL。 */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return interceptor;
    }
}
