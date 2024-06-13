package com.test.tracing.service;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

import org.springframework.web.bind.annotation.RequestBody;

@Path("/speak")
public interface SpeakService {

    @POST
    @Path("/say")
    String say(@RequestBody String msg);
}
