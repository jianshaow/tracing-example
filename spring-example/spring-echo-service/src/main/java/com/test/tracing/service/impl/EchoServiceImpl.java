package com.test.tracing.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.test.tracing.service.AuralService;
import com.test.tracing.service.EchoService;
import com.test.tracing.service.SpeakService;

@RestController
@RequestMapping("/echo")
public class EchoServiceImpl implements EchoService {

    private static Logger logger = LoggerFactory.getLogger(AuralServiceImpl.class);

    @Autowired
    private AuralService auralService;

    @Autowired
    private SpeakService speakService;

    @Override
    @RequestMapping(method = RequestMethod.POST)
    public String echo(@RequestBody String msg) {
        logger.info("be requested to echo a message: {}", msg);
        String result = msg;
        result = auralService.hear(result);
        result = speakService.say(result);
        logger.info("echo back the result: {}", result);
        return result;
    }

    public void setAuralService(AuralService auralService) {
        this.auralService = auralService;
    }

    public void setSpeakService(SpeakService speakService) {
        this.speakService = speakService;
    }
}
