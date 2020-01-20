package com.test.tracing.specialagent.rule.filter;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;

import com.test.tracing.span.CustomizedSpanDecorator;

import io.opentracing.contrib.specialagent.Logger;
import io.opentracing.contrib.web.servlet.filter.TracingFilter;
import io.opentracing.util.GlobalTracer;

public abstract class ServletFilterAgentIntercept {
  public static final Logger logger = Logger.getLogger(ServletFilterAgentIntercept.class);
  public static final Map<Object,ServletContext> filterOrServletToServletContext = new HashMap<>();
  public static final Map<ServletRequest,Boolean> servletRequestToState = new HashMap<>();
  public static final Map<ServletContext,TracingFilter> servletContextToFilter = new ConcurrentHashMap<>();

  public static TracingFilter getFilter(final ServletContext context) throws ServletException {
    Objects.requireNonNull(context);
    TracingFilter filter = servletContextToFilter.get(context);
    if (filter != null)
      return filter;

    synchronized (servletContextToFilter) {
      filter = servletContextToFilter.get(context);
      if (filter != null)
        return filter;

      servletContextToFilter.put(context, filter = new TracingFilter(GlobalTracer.get(), Collections.singletonList(new CustomizedSpanDecorator()), null));
      return filter;
    }
  }

  public static Method getMethod(final Class<?> cls, final String name, final Class<?> ... parameterTypes) {
    try {
      final Method method = cls.getMethod(name, parameterTypes);
      return Modifier.isAbstract(method.getModifiers()) ? null : method;
    }
    catch (final NoClassDefFoundError | NoSuchMethodException e) {
      return null;
    }
  }
}