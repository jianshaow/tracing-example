package com.test.tracing.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EchoServiceController {

    @Autowired
    private JdbcTemplate template;

    @RequestMapping(name = "/echo", method = RequestMethod.POST)
    public String echo(@RequestBody String msg) {
        template.execute("select * from application");
        return msg;
    }
}
