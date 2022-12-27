/* Copyright 2019 The OpenTracing Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import io.opentracing.contrib.web.servlet.filter.ServletFilterSpanDecorator;
import io.opentracing.contrib.web.servlet.filter.TracingFilter;
import io.opentracing.util.GlobalTracer;

public abstract class ServletFilterAgentIntercept {
  public static final Logger logger = Logger.getLogger(ServletFilterAgentIntercept.class);
  public static final Map<Object,ServletContext> filterOrServletToServletContext = new HashMap<>();
  public static final Map<ServletRequest,Boolean> servletRequestToState = new HashMap<>();
  public static final Map<ServletContext,TracingFilter> servletContextToFilter = new ConcurrentHashMap<>();

  public static TracingFilter getFilter(final ServletContext context, final boolean proxy) throws ServletException {
    Objects.requireNonNull(context);
    TracingFilter filter = servletContextToFilter.get(context);
    if (filter != null)
      return filter;

    synchronized (servletContextToFilter) {
      filter = servletContextToFilter.get(context);
      if (filter != null)
        return filter;

      servletContextToFilter.put(context, filter = proxy ? new TracingProxyFilter(GlobalTracer.get(), context) : new TracingFilter(GlobalTracer.get(), Collections.singletonList((ServletFilterSpanDecorator)new CustomizedSpanDecorator()), null));
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