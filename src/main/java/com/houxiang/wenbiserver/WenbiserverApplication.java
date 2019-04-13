package com.houxiang.wenbiserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//导入Mybatis对应mapper接口包
@MapperScan("com.houxiang.wenbiserver.mapper")
@EnableScheduling
public class WenbiserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(WenbiserverApplication.class, args);
    }

}

