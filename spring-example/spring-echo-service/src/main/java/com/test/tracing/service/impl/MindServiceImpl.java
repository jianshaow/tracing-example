package com.test.tracing.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.test.tracing.service.MindService;

@RestController
@RequestMapping("/mind")
public class MindServiceImpl implements MindService {

    private static Logger logger = LoggerFactory.getLogger(MindServiceImpl.class);

    @Autowired
    private JdbcTemplate template;

    @Override
    @RequestMapping(value = "/respond", method = RequestMethod.POST)
    public String respond(@RequestBody String msg) {
        final int latency = SleepUtil.sleepRandomly(100);
        logger.info("I lost my mind for {} ms.", latency);
        return "echo back: " + msg;
    }

    @Override
    @RequestMapping(value = "/recall", method = RequestMethod.POST)
    public String recall(@RequestBody String something) {
        final int latency = SleepUtil.sleepRandomly(100);
        logger.info("I lost my mind for {} ms.", latency);
        template.execute("select * from application");
        return something;
    }

    public void setTemplate(JdbcTemplate template) {
        this.template = template;
    }
}
