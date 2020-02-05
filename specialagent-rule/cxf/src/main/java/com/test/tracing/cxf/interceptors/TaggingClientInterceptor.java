package com.test.tracing.cxf.interceptors;

import java.util.Set;

import org.apache.cxf.phase.Phase;
import org.apache.cxf.tracing.opentracing.OpenTracingClientStopInterceptor;

public class TaggingClientInterceptor extends AbstractTaggingInterceptor {
	private static final String TRACE_SPAN = "org.apache.cxf.tracing.client.opentracing.span";

	public TaggingClientInterceptor() {
		super(Phase.RECEIVE);
	}

	public Set<String> getBefore() {
		final Set<String> before = super.getBefore();
		before.add(OpenTracingClientStopInterceptor.class.getName());
		return before;
	}

	@Override
	protected Object getTraceSpanKey() {
		return TRACE_SPAN;
	}
}
