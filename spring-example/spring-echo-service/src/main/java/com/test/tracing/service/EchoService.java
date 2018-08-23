package com.test.tracing.service;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/echo")
public interface EchoService {

    @POST
    String echo(String msg);
}
