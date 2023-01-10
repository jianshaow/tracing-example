package com.test.tracing.extension.sampler;

import java.util.List;

import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.context.Context;
import io.opentelemetry.sdk.trace.data.LinkData;
import io.opentelemetry.sdk.trace.samplers.Sampler;
import io.opentelemetry.sdk.trace.samplers.SamplingDecision;
import io.opentelemetry.sdk.trace.samplers.SamplingResult;

public class CustomizedSampler implements Sampler {
	
	@Override
	public SamplingResult shouldSample(Context parentContext, String traceId, String name, SpanKind spanKind,
			Attributes attributes, List<LinkData> parentLinks) {
		System.out.println("span[" + name + "] is sampled.");
		return SamplingResult.create(SamplingDecision.RECORD_AND_SAMPLE);
	}

	@Override
	public String getDescription() {
		return this.getClass().getName();
	}
}
