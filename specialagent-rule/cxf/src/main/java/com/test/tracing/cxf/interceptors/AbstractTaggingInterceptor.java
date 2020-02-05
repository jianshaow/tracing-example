package com.test.tracing.cxf.interceptors;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptor;
import org.apache.cxf.tracing.AbstractTracingProvider;
import org.apache.cxf.tracing.opentracing.TraceScope;

import io.opentracing.Span;

public abstract class AbstractTaggingInterceptor extends AbstractTracingProvider implements PhaseInterceptor<Message> {

	private String phase;

	public AbstractTaggingInterceptor(final String phase) {
		this.phase = phase;
	}

	public void handleMessage(Message message) throws Fault {
		@SuppressWarnings("unchecked")
		final TraceScopeHolder<TraceScope> holder = (TraceScopeHolder<TraceScope>) message.getExchange()
				.get(getTraceSpanKey());
		final TraceScope traceScope = holder.getScope();
		if (traceScope != null) {
			Span span = traceScope.getSpan();
			span.setTag("customized.tag", "we tag what we want");
		}
	}

	protected abstract Object getTraceSpanKey();

	public Collection<PhaseInterceptor<? extends Message>> getAdditionalInterceptors() {
		return null;
	}

	public Set<String> getAfter() {
		return new HashSet<String>();
	}

	public Set<String> getBefore() {
		return new HashSet<String>();
	}

	public String getId() {
		return getClass().getName();
	}

	public String getPhase() {
		return phase;
	}

	public void handleFault(Message message) {
	}
}
