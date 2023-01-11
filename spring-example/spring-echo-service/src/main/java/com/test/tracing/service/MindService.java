package com.test.tracing.service;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Path("/mind")
public interface MindService {

    @POST
    @Path("/recall")
    String recall(String something);

    @POST
    @Path("/respond")
    String respond(String msg);
}
