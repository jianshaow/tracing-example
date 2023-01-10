package com.test.tracing.extension.processor;

import io.opentelemetry.context.Context;
import io.opentelemetry.sdk.trace.ReadWriteSpan;
import io.opentelemetry.sdk.trace.ReadableSpan;
import io.opentelemetry.sdk.trace.SpanProcessor;

public class CustomizedProcessor implements SpanProcessor {
	
	@Override
	public void onStart(Context parentContext, ReadWriteSpan span) {
		span.setAttribute("customized tag", "test");
	}

	@Override
	public boolean isStartRequired() {
		return true;
	}

	@Override
	public void onEnd(ReadableSpan span) {
		System.out.println("span[" + span.getName() + "] ended.");
	}

	@Override
	public boolean isEndRequired() {
		return false;
	}
}
