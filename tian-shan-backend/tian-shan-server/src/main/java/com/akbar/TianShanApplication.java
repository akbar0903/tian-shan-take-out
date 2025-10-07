package com.akbar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement // 开启基于注解的事务管理
@EnableCaching               // 开启SpringCache缓存支持
public class TianShanApplication {
    public static void main(String[] args) {
        SpringApplication.run(TianShanApplication.class, args);
    }
}
