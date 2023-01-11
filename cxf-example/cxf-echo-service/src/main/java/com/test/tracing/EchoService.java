package com.test.tracing;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Path("/echo")
public interface EchoService {

    @POST
    String echo(String msg);
}
