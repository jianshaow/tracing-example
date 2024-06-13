package com.test.tracing.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.test.tracing.AuralService;
import com.test.tracing.MindService;

import io.opentelemetry.instrumentation.annotations.SpanAttribute;
import io.opentelemetry.instrumentation.annotations.WithSpan;

public class AuralServiceImpl implements AuralService {

    private static final Logger logger = LoggerFactory.getLogger(AuralServiceImpl.class);

    private MindService mindService;

    @Override
    public String hear(String msg) {
        beforeCallMindService(msg);
        return mindService.recall(msg);
    }

    @WithSpan
    protected void beforeCallMindService(@SpanAttribute("msg") String msg) {
    	final int latency = SleepUtil.sleepRandomly(50);
        logger.info("I got the msg finally in {} ms.", latency);
    }

    public void setMindService(MindService mindService) {
        this.mindService = mindService;
    }
}
