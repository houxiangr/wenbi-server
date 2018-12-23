package com.houxiang.wenbiserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//导入Mybatis对应mapper接口包
@MapperScan("com.houxiang.wenbiserver.mapper")
public class WenbiserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(WenbiserverApplication.class, args);
    }

}

