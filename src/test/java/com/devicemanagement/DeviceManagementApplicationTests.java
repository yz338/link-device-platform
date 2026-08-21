package com.devicemanagement;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class DeviceManagementApplicationTests {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String dbHash = "$2b$10$7q65ifp6kWGjXzZ8jZpT3.MrIxAIQCMWKA25WIvXESrCPEkt08I46";

        // 换成你登录时输入的密码逐个试
        for (String pwd : new String[]{"123456", "admin", "admin123", "12345678"}) {
            System.out.println(pwd + " -> " + encoder.matches(pwd, dbHash));
        }
    }

}
