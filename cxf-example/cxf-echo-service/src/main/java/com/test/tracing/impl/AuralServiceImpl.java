package com.test.tracing.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.test.tracing.AuralService;
import com.test.tracing.MindService;

public class AuralServiceImpl implements AuralService {

    private static Logger logger = LoggerFactory.getLogger(AuralServiceImpl.class);

    private MindService mindService;

    @Override
    public String hear(String msg) {
        final int latency = SleepUtil.sleepRandomly(50);
        logger.info("I got the msg finally in {} ms.", latency);
        beforeCallMindService();
        final String result = mindService.recall(msg);
        return result;
    }

    protected void beforeCallMindService() {
        // do nothing
    }

    public void setMindService(MindService mindService) {
        this.mindService = mindService;
    }
}
