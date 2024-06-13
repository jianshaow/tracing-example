package com.test.tracing.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.test.tracing.service.AuralService;
import com.test.tracing.service.MindService;

@RestController
@RequestMapping("/aural")
public class AuralServiceImpl implements AuralService {

    private static final Logger logger = LoggerFactory.getLogger(AuralServiceImpl.class);

    @Autowired
    private MindService mindService;

    @Override
    @RequestMapping(value = "/hear", method = RequestMethod.POST)
    public String hear(@RequestBody String msg) {
        final int latency = SleepUtil.sleepRandomly(100);
        logger.info("I got the msg finally in {} ms.", latency);
        return mindService.recall(msg);
    }

    public void setMindService(MindService mindService) {
        this.mindService = mindService;
    }
}
