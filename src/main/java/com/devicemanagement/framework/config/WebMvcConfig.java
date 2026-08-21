package com.devicemanagement.framework.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 把本地磁盘上的上传目录映射为 /upload/** 静态资源，供前端访问图片。
 * <p>
 * upload-dir 配置为相对路径（upload），以 user.dir（项目运行目录）为基准解析为绝对路径。
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${app.upload-dir:upload}")
    private String uploadDir;

    private Path absUploadPath;

    @PostConstruct
    public void init() {
        absUploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    public Path getAbsUploadPath() {
        return absUploadPath;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        try {
            Files.createDirectories(absUploadPath);
        } catch (Exception ignored) {
            // 映射前尽量确保根目录存在，忽略失败，真正写入时再兜底创建
        }
        // /upload/workorder/xxx.jpg -> {app.upload-dir}/workorder/xxx.jpg
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:" + absUploadPath.toString().replace("\\", "/") + "/");
    }
}

