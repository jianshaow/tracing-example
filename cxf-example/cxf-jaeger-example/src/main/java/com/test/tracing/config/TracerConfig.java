package com.test.tracing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.jaegertracing.internal.JaegerTracer;
import io.jaegertracing.internal.propagation.B3TextMapCodec;
import io.jaegertracing.internal.reporters.RemoteReporter;
import io.jaegertracing.internal.samplers.ConstSampler;
import io.jaegertracing.spi.Reporter;
import io.jaegertracing.thrift.internal.senders.HttpSender;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format;
import io.opentracing.util.GlobalTracer;

@Configuration
public class TracerConfig {

    @Bean
    public Tracer tracer() {
        final HttpSender sender = new HttpSender.Builder("http://localhost:14268/api/traces").build();
        final Reporter reporter = new RemoteReporter.Builder().withSender(sender).build();
        //        final Reporter reporter = new LoggingReporter();
        final ConstSampler sampler = new ConstSampler(true);
        final B3TextMapCodec b3Codec = new B3TextMapCodec.Builder().build();
        final Tracer tracer = new JaegerTracer.Builder("echo-service")
                .registerInjector(Format.Builtin.HTTP_HEADERS, b3Codec)
                .registerExtractor(Format.Builtin.HTTP_HEADERS, b3Codec).withReporter(reporter).withSampler(sampler)
                .build();
        GlobalTracer.registerIfAbsent(tracer);
        return tracer;
    }

}
