package com.test.opentracing.cxf;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;

import io.opentracing.Tracer;

public class ClientStopInterceptor extends AbstractClientInterceptor {

    public ClientStopInterceptor(final Tracer tracer) {
        super(Phase.RECEIVE, tracer);
    }

    public ClientStopInterceptor(final String phase, final Tracer tracer) {
        super(phase, tracer);
    }

    @Override
    public void handleMessage(Message message) throws Fault {
        @SuppressWarnings("unchecked")
        final TraceScopeHolder<TraceScope> holder = (TraceScopeHolder<TraceScope>) message.getExchange()
                .get(TRACE_SPAN);

        Integer responseCode = (Integer) message.get(Message.RESPONSE_CODE);
        if (responseCode == null) {
            responseCode = 200;
        }

        super.stopTraceSpan(holder, responseCode);
    }
}
