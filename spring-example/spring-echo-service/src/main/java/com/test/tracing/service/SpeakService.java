package com.test.tracing.service;

import org.springframework.web.bind.annotation.RequestBody;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Path("/speak")
public interface SpeakService {

    @POST
    @Path("/say")
    String say(@RequestBody String msg);
}
