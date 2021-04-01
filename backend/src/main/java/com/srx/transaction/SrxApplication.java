package com.srx.transaction;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.srx.transaction.Mapper")
@EnableScheduling
public class SrxApplication {
    public static void main(String[] args) {
        SpringApplication.run(SrxApplication.class, args);

    }
}

