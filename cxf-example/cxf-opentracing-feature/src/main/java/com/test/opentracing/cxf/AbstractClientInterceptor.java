package com.test.opentracing.cxf;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.apache.cxf.jaxrs.utils.JAXRSUtils;
import org.apache.cxf.message.Message;

import com.test.opentracing.CxfHeadersTextMap;

import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format.Builtin;
import io.opentracing.tag.Tags;

public abstract class AbstractClientInterceptor extends AbstractInterceptor {

    protected AbstractClientInterceptor(final String phase, final Tracer tracer) {
        super(phase, tracer);
    }

    @Override
    public void handleFault(@SuppressWarnings("unused") Message message) {
        // do nothing
    }

    protected TraceScopeHolder<TraceScope> startTraceSpan(final Map<String, List<String>> requestHeaders, URI uri,
            String method) {
        final Span parent = getTracer().activeSpan();
        Scope scope = null;
        if (parent == null) {
            scope = getTracer().buildSpan(buildSpanDescription(uri.toString(), method)).startActive(false);
        } else {
            scope = getTracer().buildSpan(buildSpanDescription(uri.toString(), method)).asChildOf(parent)
                    .startActive(false);
        }

        scope.span().setTag(Tags.SPAN_KIND.getKey(), Tags.SPAN_KIND_CLIENT);
        scope.span().setTag(Tags.HTTP_METHOD.getKey(), method);
        scope.span().setTag(Tags.HTTP_URL.getKey(), uri.toString());

        getTracer().inject(scope.span().context(), Builtin.HTTP_HEADERS, new CxfHeadersTextMap(requestHeaders));

        Span span = null;
        if (isAsyncInvocation()) {
            span = scope.span();
            scope.close();
        }

        return new TraceScopeHolder<TraceScope>(new TraceScope(span, scope), span != null);
    }

    protected void stopTraceSpan(final TraceScopeHolder<TraceScope> holder, final int responseStatus) {
        if (holder == null) {
            return;
        }

        final TraceScope traceScope = holder.getScope();
        if (traceScope != null) {
            Span span = traceScope.getSpan();
            Scope scope = traceScope.getScope();

            if (holder.isDetached()) {
                scope = getTracer().scopeManager().activate(span, false);
            }

            scope.span().setTag(Tags.HTTP_STATUS.getKey(), responseStatus);
            scope.span().finish();

            scope.close();
        }
    }

    private boolean isAsyncInvocation() {
        return !JAXRSUtils.getCurrentMessage().getExchange().isSynchronous();
    }
}
