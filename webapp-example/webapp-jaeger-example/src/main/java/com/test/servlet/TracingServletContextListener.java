package com.test.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import io.jaegertracing.Configuration;
import io.jaegertracing.internal.reporters.RemoteReporter;
import io.jaegertracing.internal.samplers.ConstSampler;
import io.jaegertracing.thrift.internal.senders.HttpSender;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;

public class TracingServletContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(@SuppressWarnings("unused") ServletContextEvent servletContextEvent) {
        final Configuration configuration = new Configuration("echoService");
        final HttpSender sender = new HttpSender.Builder("http://localhost:14268/api/traces").build();
        final RemoteReporter reporter = new RemoteReporter.Builder().withSender(sender).build();
        final ConstSampler sampler = new ConstSampler(true);
        final Tracer tracer = configuration.getTracerBuilder().withReporter(reporter).withSampler(sampler).build();
        GlobalTracer.register(tracer);
    }

    @Override
    public void contextDestroyed(@SuppressWarnings("unused") ServletContextEvent servletContextEvent) {
        // do nothing
    }
}
