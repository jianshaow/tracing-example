package com.test.tracing.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.test.tracing.MindService;

import io.opentelemetry.instrumentation.annotations.WithSpan;

public class MindServiceImpl implements MindService {

    private static Logger logger = LoggerFactory.getLogger(MindServiceImpl.class);

    private JdbcTemplate template;

    @Override
    public String respond(String msg) {
        final int latency = SleepUtil.sleepRandomly(50);
        logger.info("I lost my mind for {} ms.", latency);
        return "echo back: " + msg;
    }

    @Override
    public String recall(String something) {
        beforeCallMemory();
        template.execute("select * from application");
        return something;
    }

    @WithSpan
    protected void beforeCallMemory() {
        final int latency = SleepUtil.sleepRandomly(50);
        logger.info("I lost my mind for {} ms.", latency);
    }

    public void setTemplate(JdbcTemplate template) {
        this.template = template;
    }
}
