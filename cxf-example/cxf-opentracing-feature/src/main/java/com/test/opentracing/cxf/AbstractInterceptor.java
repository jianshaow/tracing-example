package com.test.opentracing.cxf;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.apache.cxf.common.util.StringUtils;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptor;

import io.opentracing.Tracer;

public abstract class AbstractInterceptor implements PhaseInterceptor<Message> {

    protected static final String TRACE_SPAN = "org.apache.cxf.tracing.client.opentracing.span";

    private String phase;

    private final Tracer tracer;

    protected AbstractInterceptor(final String phase, final Tracer tracer) {
        this.phase = phase;
        this.tracer = tracer;
    }

    protected Tracer getTracer() {
        return tracer;
    }

    @Override
    public Collection<PhaseInterceptor<? extends Message>> getAdditionalInterceptors() {
        return null;
    }

    @Override
    public Set<String> getAfter() {
        return Collections.emptySet();
    }

    @Override
    public Set<String> getBefore() {
        return Collections.emptySet();
    }

    @Override
    public String getId() {
        return getClass().getName();
    }

    @Override
    public String getPhase() {
        return phase;
    }

    @Override
    public void handleFault(@SuppressWarnings("unused") Message message) {
        // do nothing
    }

    protected String buildSpanDescription(final String path, final String method) {
        if (StringUtils.isEmpty(method)) {
            return path;
        }
        return method + " " + path;
    }

    protected static URI getUri(Message message) {
        try {
            String uriSt = getUriAsString(message);
            return uriSt != null ? new URI(uriSt) : new URI("");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static String getUriAsString(Message message) {
        String uri = safeGet(message, Message.REQUEST_URL);

        if (uri == null) {
            String address = safeGet(message, Message.ENDPOINT_ADDRESS);
            uri = safeGet(message, Message.REQUEST_URI);
            if (uri != null && uri.startsWith("/")) {
                if (address != null && !address.startsWith(uri)) {
                    if (address.endsWith("/") && address.length() > 1) {
                        address = address.substring(0, address.length());
                    }
                    uri = address + uri;
                }
            } else {
                uri = address;
            }
        }
        String query = safeGet(message, Message.QUERY_STRING);
        if (query != null) {
            return uri + "?" + query;
        }

        return uri;
    }

    private static String safeGet(Message message, String key) {
        if (!message.containsKey(key)) {
            return null;
        }
        Object value = message.get(key);
        return (value instanceof String) ? value.toString() : null;
    }

    protected static class TraceScopeHolder<T> implements Serializable {
        private static final long serialVersionUID = -5985783659818936359L;

        private final T scope;
        private final boolean detached;

        public TraceScopeHolder(final T scope, final boolean detached) {
            this.scope = scope;
            this.detached = detached;
        }

        public T getScope() {
            return scope;
        }

        public boolean isDetached() {
            return detached;
        }
    }
}
