package com.test.tracing.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JaegerExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(JaegerExampleApplication.class, args);
    }
}
