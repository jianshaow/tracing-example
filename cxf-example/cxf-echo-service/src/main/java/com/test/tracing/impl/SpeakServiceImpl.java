package com.test.tracing.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.test.tracing.MindService;
import com.test.tracing.SpeakService;

public class SpeakServiceImpl implements SpeakService {

    private static Logger logger = LoggerFactory.getLogger(SpeakServiceImpl.class);

    private MindService mindService;

    @Override
    public String say(String msg) {
        beforeCallMindService();
        final String result = mindService.respond(msg);
        final int latency = SleepUtil.sleepRandomly(50);
        logger.info("I stuttered for {} ms.", latency);
        return result;
    }

    protected void beforeCallMindService() {
        // do nothing
    }

    public void setMindService(MindService mindService) {
        this.mindService = mindService;
    }
}
