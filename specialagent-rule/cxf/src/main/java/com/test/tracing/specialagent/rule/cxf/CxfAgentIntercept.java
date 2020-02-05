package com.test.tracing.specialagent.rule.cxf;

import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.client.JAXRSClientFactoryBean;
import org.apache.cxf.tracing.opentracing.OpenTracingClientFeature;
import org.apache.cxf.tracing.opentracing.OpenTracingFeature;

import com.test.tracing.cxf.interceptors.TaggingClientInterceptor;
import com.test.tracing.cxf.interceptors.TaggingServerInterceptor;

import io.opentracing.util.GlobalTracer;

public class CxfAgentIntercept {

	public static void addRsClientTracingFeature(final Object thiz) {
		final JAXRSClientFactoryBean factoryBean = (JAXRSClientFactoryBean) thiz;
		factoryBean.getFeatures().add(new OpenTracingClientFeature(GlobalTracer.get()));
		factoryBean.getInInterceptors().add(new TaggingClientInterceptor());
	}

	public static void addRsServerTracingFeauture(final Object thiz) {
		final JAXRSServerFactoryBean factoryBean = (JAXRSServerFactoryBean) thiz;
		factoryBean.getFeatures().add(new OpenTracingFeature(GlobalTracer.get()));
		factoryBean.getOutInterceptors().add(new TaggingServerInterceptor());
	}
}