package com.test.tracing;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Path("/speak")
public interface SpeakService {

    @POST
    @Path("/say")
    String say(String msg);
}
