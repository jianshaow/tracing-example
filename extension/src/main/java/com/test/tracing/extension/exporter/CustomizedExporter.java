package com.test.tracing.extension.exporter;

import java.util.Collection;

import io.opentelemetry.sdk.common.CompletableResultCode;
import io.opentelemetry.sdk.trace.data.SpanData;
import io.opentelemetry.sdk.trace.export.SpanExporter;

public class CustomizedExporter implements SpanExporter {
	
	@Override
	public CompletableResultCode export(Collection<SpanData> spans) {
		System.out.println(spans.size() + " spans are about to be exported.");
		return CompletableResultCode.ofSuccess();
	}

	@Override
	public CompletableResultCode flush() {
		return CompletableResultCode.ofSuccess();
	}

	@Override
	public CompletableResultCode shutdown() {
		return CompletableResultCode.ofSuccess();
	}
}
