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

	private final Sampler root;

	public CustomizedSampler(Sampler root) {
		this.root = root;
	}

	@Override
	public SamplingResult shouldSample(Context parentContext, String traceId, String name, SpanKind spanKind,
			Attributes attributes, List<LinkData> parentLinks) {
		if (spanKind == SpanKind.CLIENT && parentContext == Context.root()) {
			System.out.println("span[" + name + "] will be recorded only.");
			return SamplingResult.create(SamplingDecision.RECORD_ONLY);
		}
		System.out.println("span[" + name + "] will be sampled.");
		return this.root.shouldSample(parentContext, traceId, name, spanKind, attributes, parentLinks);
	}

	@Override
	public String getDescription() {
		return this.getClass().getName();
	}
}
