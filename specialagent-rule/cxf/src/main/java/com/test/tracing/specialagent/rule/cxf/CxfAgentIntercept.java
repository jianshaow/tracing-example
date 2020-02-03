package com.test.tracing.specialagent.rule.cxf;

import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.client.JAXRSClientFactoryBean;
import org.apache.cxf.tracing.opentracing.OpenTracingClientFeature;
import org.apache.cxf.tracing.opentracing.OpenTracingFeature;

import io.opentracing.util.GlobalTracer;

public class CxfAgentIntercept {

	public static void addClientTracingFeature(final Object thiz) {
		final JAXRSClientFactoryBean factoryBean = (JAXRSClientFactoryBean) thiz;
		factoryBean.getFeatures().add(new OpenTracingClientFeature(GlobalTracer.get()));
	}

	public static void addServerTracingFeauture(final Object thiz) {
		final JAXRSServerFactoryBean factoryBean = (JAXRSServerFactoryBean) thiz;
		factoryBean.getFeatures().add(new OpenTracingFeature(GlobalTracer.get()));
	}
}