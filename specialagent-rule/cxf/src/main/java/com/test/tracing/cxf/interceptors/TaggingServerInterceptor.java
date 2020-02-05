package com.test.tracing.cxf.interceptors;

import java.util.Set;

import org.apache.cxf.phase.Phase;
import org.apache.cxf.tracing.opentracing.OpenTracingStopInterceptor;

public class TaggingServerInterceptor extends AbstractTaggingInterceptor {

	private static final String TRACE_SPAN = "org.apache.cxf.tracing.opentracing.span";

	public TaggingServerInterceptor() {
		super(Phase.PRE_MARSHAL);
	}

	public Set<String> getBefore() {
		final Set<String> before = super.getBefore();
		before.add(OpenTracingStopInterceptor.class.getName());
		return before;
	}

	@Override
	protected Object getTraceSpanKey() {
		return TRACE_SPAN;
	}
}
