package com.devicemanagement.framework.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据库连接检查配置。
 *
 * <p>连接池默认会在首次执行 SQL 时才创建连接。该检查使数据库地址、账号或密码配置错误时，
 * 应用在启动阶段就输出完整错误日志并停止启动。</p>
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class DatabaseConnectionCheckConfig {

    private final DataSource dataSource;

    /**
     * 创建应用启动后的数据库连通性检查任务。
     *
     * @return 数据库连接检查任务
     */
    @Bean
    public ApplicationRunner databaseConnectionCheckRunner() {
        return arguments -> {
            try (Connection connection = dataSource.getConnection()) {
                log.info("数据库连接检查通过：{}", connection.getMetaData().getURL());
            } catch (SQLException exception) {
                log.error("数据库连接失败，请检查 spring.datasource 的 url、username、password 配置", exception);
                throw new IllegalStateException("数据库连接失败，应用停止启动", exception);
            }
        };
    }
}
