package com.test.tracing.service.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.test.tracing.service.MindService;

@RestController
@RequestMapping("/mind")
public class MindServiceImpl implements MindService {

    private static final Logger logger = LoggerFactory.getLogger(MindServiceImpl.class);

    @Autowired
    private JdbcTemplate template;

    @Autowired
    private RedissonClient redissonClient;

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
        final int appId = 1;
        final RBucket<String> bucket = redissonClient.getBucket(Integer.toString(appId));
        String appName = bucket.get();
        if (appName == null) {
            appName = template.execute("select name from application where id = ?",
                    new PreparedStatementCallback<String>() {
                        @Override
                        public String doInPreparedStatement(PreparedStatement ps)
                                throws SQLException, DataAccessException {
                            ps.setInt(1, appId);
                            final ResultSet result = ps.executeQuery();
                            if (result.next()) {
                                return result.getString(1);
                            }
                            return null;
                        }
                    });
            if (appName != null) {
                bucket.set(appName, 10, TimeUnit.SECONDS);
            }
        }
        logger.info("I found the app name: {}.", appName);
        return something;
    }

    public void setTemplate(JdbcTemplate template) {
        this.template = template;
    }
}
