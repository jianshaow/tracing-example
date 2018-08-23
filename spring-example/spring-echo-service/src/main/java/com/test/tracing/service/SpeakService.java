package com.test.tracing.service;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.springframework.web.bind.annotation.RequestBody;

@Path("/speak")
public interface SpeakService {

    @POST
    @Path("/say")
    String say(@RequestBody String msg);
}
