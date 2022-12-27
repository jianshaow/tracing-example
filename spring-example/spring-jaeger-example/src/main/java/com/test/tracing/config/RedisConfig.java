package com.test.tracing.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.opentracing.Tracer;
import io.opentracing.contrib.redis.common.TracingConfiguration;
import io.opentracing.contrib.redis.redisson.TracingRedissonClient;

@Configuration
public class RedisConfig {

    @Autowired
    private Tracer tracer;

    @Bean
    public RedissonClient redissonClient() {
        final Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6379").setClientName("echo-service");
        final RedissonClient redissonClient = Redisson.create(config);
        final TracingConfiguration tracingConfig = new TracingConfiguration.Builder(tracer)
                .traceWithActiveSpanOnly(true).build();
        final RedissonClient tracingRedissonClient = new TracingRedissonClient(redissonClient, tracingConfig);
        return tracingRedissonClient;
    }
}
