package com.test.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jaegertracing.internal.JaegerTracer;
import io.jaegertracing.internal.propagation.B3TextMapCodec;
import io.jaegertracing.internal.reporters.RemoteReporter;
import io.jaegertracing.internal.samplers.ConstSampler;
import io.jaegertracing.thrift.internal.senders.HttpSender;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format;
import io.opentracing.util.GlobalTracer;

public class TracingServletContextListener implements ServletContextListener {

	private static Logger logger = LoggerFactory.getLogger(TracingServletContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
			final HttpSender sender = new HttpSender.Builder("http://localhost:14268/api/traces").build();
			final RemoteReporter reporter = new RemoteReporter.Builder().withSender(sender).build();
			final ConstSampler sampler = new ConstSampler(true);
			final B3TextMapCodec b3Codec = new B3TextMapCodec.Builder().build();
			final Tracer tracer = new JaegerTracer.Builder("echo-service")
			        .registerInjector(Format.Builtin.HTTP_HEADERS, b3Codec)
			        .registerExtractor(Format.Builtin.HTTP_HEADERS, b3Codec).withReporter(reporter).withSampler(sampler)
			        .build();
			GlobalTracer.registerIfAbsent(tracer);
		} catch (TTransportException e) {
			logger.error("build jaeger sender failed", e);
		}
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        // do nothing
    }
}
