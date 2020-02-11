package com.test.tracing.servlet;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.opentracing.Span;
import io.opentracing.contrib.web.servlet.filter.ServletFilterSpanDecorator;
import io.opentracing.tag.Tags;

public class CustomizedSpanDecorator implements ServletFilterSpanDecorator {

	@Override
	public void onRequest(HttpServletRequest httpServletRequest, Span span) {
		Tags.COMPONENT.set(span, "java-web-servlet");

		Tags.HTTP_METHOD.set(span, httpServletRequest.getMethod());
		// without query params
		Tags.HTTP_URL.set(span, httpServletRequest.getRequestURL().toString());
		
		span.setTag("customized.tag", "we tag what we want");
	}

	@Override
	public void onResponse(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Span span) {
		Tags.HTTP_STATUS.set(span, httpServletResponse.getStatus());
	}

	@Override
	public void onError(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Throwable exception, Span span) {
		Tags.ERROR.set(span, Boolean.TRUE);
		span.log(logsForException(exception));

		if (httpServletResponse.getStatus() == HttpServletResponse.SC_OK) {
			// exception is thrown in filter chain, but status code is incorrect
			Tags.HTTP_STATUS.set(span, 500);
		}
	}

	@Override
	public void onTimeout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, long timeout,
			Span span) {
		Map<String, Object> timeoutLogs = new HashMap<>(2);
		timeoutLogs.put("event", "timeout");
		timeoutLogs.put("timeout", timeout);
		span.log(timeoutLogs);
	}

	private Map<String, String> logsForException(Throwable throwable) {
		Map<String, String> errorLog = new HashMap<>(3);
		errorLog.put("event", Tags.ERROR.getKey());

		String message = throwable.getCause() != null ? throwable.getCause().getMessage() : throwable.getMessage();
		if (message != null) {
			errorLog.put("message", message);
		}
		StringWriter sw = new StringWriter();
		throwable.printStackTrace(new PrintWriter(sw));
		errorLog.put("stack", sw.toString());

		return errorLog;
	}
}
