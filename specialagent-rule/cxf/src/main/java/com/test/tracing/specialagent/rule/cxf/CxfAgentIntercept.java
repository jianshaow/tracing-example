package com.test.tracing.specialagent.rule.cxf;

import org.apache.cxf.endpoint.AbstractEndpointFactory;
import org.apache.cxf.tracing.opentracing.OpenTracingClientFeature;
import org.apache.cxf.tracing.opentracing.OpenTracingFeature;

import com.test.tracing.cxf.interceptors.TaggingClientInterceptor;
import com.test.tracing.cxf.interceptors.TaggingServerInterceptor;

import io.opentracing.util.GlobalTracer;

public class CxfAgentIntercept {

	public static void addClientTracingFeature(final Object thiz) {
		final AbstractEndpointFactory factory = (AbstractEndpointFactory) thiz;
		factory.getFeatures().add(new OpenTracingClientFeature(GlobalTracer.get()));
		factory.getInInterceptors().add(new TaggingClientInterceptor());
	}

	public static void addServerTracingFeauture(final Object thiz) {
		final AbstractEndpointFactory factory = (AbstractEndpointFactory) thiz;
		factory.getFeatures().add(new OpenTracingFeature(GlobalTracer.get()));
		factory.getOutInterceptors().add(new TaggingServerInterceptor());
	}
}