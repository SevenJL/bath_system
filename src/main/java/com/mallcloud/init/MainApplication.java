package com.mallcloud.init;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 主类（项目启动入口）
 * @author Admin
 */
@SpringBootApplication // Spring Boot 应用 
@MapperScan("com.mallcloud.init.mapper") // 扫描 Mapper 接口
public class MainApplication {
    public static void main(String[] args) {
        // 1. 启动 Spring Boot 应用
        SpringApplication.run(MainApplication.class, args);
    }
}
