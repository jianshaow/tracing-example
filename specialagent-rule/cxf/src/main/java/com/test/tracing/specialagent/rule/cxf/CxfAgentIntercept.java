package com.test.tracing.specialagent.rule.cxf;

import java.lang.reflect.InvocationTargetException;

import io.opentracing.Tracer;
import io.opentracing.contrib.specialagent.Level;
import io.opentracing.contrib.specialagent.Logger;
import io.opentracing.util.GlobalTracer;

public class CxfAgentIntercept {

	private static final Logger logger = Logger.getLogger(CxfAgentIntercept.class);

	public static void addClientTracingFeature(final Object thiz) {
//		final JAXRSClientFactoryBean factoryBean = (JAXRSClientFactoryBean) thiz;
//		factoryBean.getFeatures().add(new OpenTracingClientFeature(GlobalTracer.get()));

		try {
			final Object features = thiz.getClass().getMethod("getFeatures").invoke(thiz);
			final Object feature = Class.forName("org.apache.cxf.tracing.opentracing.OpenTracingClientFeature")
					.getConstructor(Tracer.class).newInstance(GlobalTracer.get());
			features.getClass().getMethod("add", Object.class).invoke(features, feature);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | ClassNotFoundException | InstantiationException e) {
			logger.log(Level.SEVERE, "fail to add tracing feature to cxf client", e);
		}
	}

	public static void addServerTracingFeauture(final Object thiz) {
//		final JAXRSServerFactoryBean factoryBean = (JAXRSServerFactoryBean) thiz;
//		factoryBean.getFeatures().add(new OpenTracingFeature(GlobalTracer.get()));

		try {
			final Object features = thiz.getClass().getMethod("getFeatures").invoke(thiz);
			final Object feature = Class.forName("org.apache.cxf.tracing.opentracing.OpenTracingFeature")
					.getConstructor(Tracer.class).newInstance(GlobalTracer.get());
			features.getClass().getMethod("add", Object.class).invoke(features, feature);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | ClassNotFoundException | InstantiationException e) {
			logger.log(Level.SEVERE, "fail to add tracing feature to cxf server", e);
		}
	}
}