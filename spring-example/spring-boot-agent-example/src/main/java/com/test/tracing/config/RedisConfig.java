package com.test.tracing.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {

    @Bean
    public RedissonClient redissonClient() {
        final Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6379").setClientName("echo-service");
        final RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;
    }
}
