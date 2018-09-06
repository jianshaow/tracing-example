package com.test.opentracing.cxf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.cxf.helpers.CastUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageUtils;
import org.apache.cxf.phase.Phase;

import io.opentracing.Tracer;

public class ServerStopInterceptor extends AbstractServerInterceptor {

    public ServerStopInterceptor(final Tracer tracer) {
        super(Phase.PRE_MARSHAL, tracer);
    }

    public ServerStopInterceptor(final String phase, final Tracer tracer) {
        super(phase, tracer);
    }

    @Override
    public void handleMessage(Message message) throws Fault {
        Map<String, List<Object>> responseHeaders = CastUtils.cast((Map<?, ?>) message.get(Message.PROTOCOL_HEADERS));

        if (responseHeaders == null) {
            responseHeaders = new HashMap<>();
            message.put(Message.PROTOCOL_HEADERS, responseHeaders);
        }

        boolean isRequestor = MessageUtils.isRequestor(message);
        Message requestMessage = isRequestor ? message.getExchange().getOutMessage()
                : message.getExchange().getInMessage();
        Map<String, List<String>> requestHeaders = CastUtils
                .cast((Map<?, ?>) requestMessage.get(Message.PROTOCOL_HEADERS));

        @SuppressWarnings("unchecked")
        final TraceScopeHolder<TraceScope> holder = (TraceScopeHolder<TraceScope>) message.getExchange()
                .get(TRACE_SPAN);

        Integer responseCode = (Integer) message.get(Message.RESPONSE_CODE);
        if (responseCode == null) {
            responseCode = 200;
        }

        super.stopTraceSpan(requestHeaders, responseHeaders, responseCode, holder);
    }
}
