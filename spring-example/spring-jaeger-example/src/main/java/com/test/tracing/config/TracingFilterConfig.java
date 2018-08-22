package com.test.tracing.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.opentracing.Tracer;
import io.opentracing.contrib.web.servlet.filter.TracingFilter;

@Configuration
public class TracingFilterConfig {

    @Autowired
    private Tracer tracer;

    @Bean
    public TracingFilter tracingFilter() {
        return new TracingFilter(tracer);
    }
}
