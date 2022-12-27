package com.test.tracing.specialagent.rule.cxfspan;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.cxf.tracing.opentracing.TraceScope;
import io.opentracing.Span;
import io.opentracing.tag.Tags;

public class CxfSpanAgentIntercept {

  public static void stopTracingSpan(final Object request, final String component,
      final String spanKind) {
    Method method = null;
    try {
      method = request.getClass().getMethod("getScope");
      method.setAccessible(true);
      final TraceScope traceScope = (TraceScope) method.invoke(request);
      if (traceScope != null) {
        Span span = traceScope.getSpan();
        span.setTag(Tags.COMPONENT, component);
        span.setTag(Tags.SPAN_KIND, spanKind);
      }
    } catch (NoSuchMethodException | SecurityException | IllegalAccessException
        | IllegalArgumentException | InvocationTargetException e) {
      e.printStackTrace();
    } finally {
      if (method != null) {
        method.setAccessible(false);
      }
    }
  }
}
