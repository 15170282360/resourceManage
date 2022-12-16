package com.example.jitpms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.jitpms.dao")
public class JitpmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(JitpmsApplication.class, args);
    }

}
