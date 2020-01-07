package com.test.tracing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.test.tracing.service.AuralService;
import com.test.tracing.service.EchoService;
import com.test.tracing.service.MindService;
import com.test.tracing.service.SpeakService;

import feign.Feign;
import feign.jaxrs.JAXRSContract;
import feign.okhttp.OkHttpClient;

@Configuration
public class ClientConfig {

    @Bean
    public AuralService auralService() {
        return Feign.builder().client(new OkHttpClient()).contract(new JAXRSContract())
                .target(AuralService.class, "http://localhost:8080/");
    }

    @Bean
    public EchoService echoService() {
        return Feign.builder().client(new OkHttpClient()).contract(new JAXRSContract())
                .target(EchoService.class, "http://localhost:8080/");
    }

    @Bean
    public MindService mindService() {
        return Feign.builder().client(new OkHttpClient()).contract(new JAXRSContract())
                .target(MindService.class, "http://localhost:8080/");
    }

    @Bean
    public SpeakService speakService() {
        return Feign.builder().client(new OkHttpClient()).contract(new JAXRSContract())
                .target(SpeakService.class, "http://localhost:8080/");
    }
}
