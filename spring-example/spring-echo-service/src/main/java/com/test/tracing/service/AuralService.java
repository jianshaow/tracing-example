package com.test.tracing.service;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/aural")
public interface AuralService {

    @POST
    @Path("/hear")
    String hear(String msg);
}
