package com.test.tracing.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.test.tracing.service.MindService;
import com.test.tracing.service.SpeakService;

@RestController
@RequestMapping("/speak")
public class SpeakServiceImpl implements SpeakService {

    private static final Logger logger = LoggerFactory.getLogger(SpeakServiceImpl.class);

    @Autowired
    private MindService mindService;

    @Override
    @RequestMapping(value = "/say", method = RequestMethod.POST)
    public String say(@RequestBody String msg) {
        final String result = mindService.respond(msg);
        final int latency = SleepUtil.sleepRandomly(100);
        logger.info("I stuttered for {} ms.", latency);
        return result;
    }

    public void setMindService(MindService mindService) {
        this.mindService = mindService;
    }
}
