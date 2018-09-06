package com.test.opentracing.cxf;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptorChain;

import com.test.opentracing.CxfHeadersTextMap;

import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format.Builtin;
import io.opentracing.tag.Tags;

public abstract class AbstractServerInterceptor extends AbstractInterceptor {

    protected AbstractServerInterceptor(final String phase, final Tracer tracer) {
        super(phase, tracer);
    }

    @Override
    public void handleFault(@SuppressWarnings("unused") Message message) {
        // do nothing
    }

    protected TraceScopeHolder<TraceScope> startTraceSpan(final Map<String, List<String>> requestHeaders, URI uri,
            String method) {

        SpanContext parent = getTracer().extract(Builtin.HTTP_HEADERS, new CxfHeadersTextMap(requestHeaders));

        Scope scope = null;
        if (parent == null) {
            scope = getTracer().buildSpan(buildSpanDescription(uri.getPath(), method)).startActive(false);
        } else {
            scope = getTracer().buildSpan(buildSpanDescription(uri.getPath(), method)).asChildOf(parent)
                    .startActive(false);
        }

        scope.span().setTag(Tags.SPAN_KIND.getKey(), Tags.SPAN_KIND_SERVER);
        scope.span().setTag(Tags.HTTP_METHOD.getKey(), method);
        scope.span().setTag(Tags.HTTP_URL.getKey(), uri.toString());

        Span span = null;
        if (isAsyncResponse()) {
            span = scope.span();
            propagateContinuationSpan(span);
            scope.close();
        }

        return new TraceScopeHolder<TraceScope>(new TraceScope(span, scope), span != null);
    }

    protected void stopTraceSpan(@SuppressWarnings("unused")
    final Map<String, List<String>> requestHeaders, @SuppressWarnings("unused")
    final Map<String, List<Object>> responseHeaders, final int responseStatus,
            final TraceScopeHolder<TraceScope> holder) {

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

    private boolean isAsyncResponse() {
        return !PhaseInterceptorChain.getCurrentMessage().getExchange().isSynchronous();
    }

    private void propagateContinuationSpan(final Span continuation) {
        PhaseInterceptorChain.getCurrentMessage().put(Span.class, continuation);
    }
}
