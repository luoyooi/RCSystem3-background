package com.luo.cv;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@MapperScan(basePackages = "com.luo.cv.mapper")
@SpringBootApplication
@EnableCaching
@EnableScheduling
@EnableAsync
public class CvApplication {
    public static void main(String[] args) {
        SpringApplication.run(CvApplication.class, args);
    }
}
