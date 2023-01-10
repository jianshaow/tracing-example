package com.test.tracing.extension.sampler;

import com.google.auto.service.AutoService;

import io.opentelemetry.sdk.autoconfigure.spi.ConfigProperties;
import io.opentelemetry.sdk.autoconfigure.spi.traces.ConfigurableSamplerProvider;
import io.opentelemetry.sdk.trace.samplers.Sampler;

@AutoService(ConfigurableSamplerProvider.class)
public class CustomizedConfigurableSamplerProvider implements ConfigurableSamplerProvider {

	@Override
	public Sampler createSampler(ConfigProperties config) {
		return new CustomizedSampler();
	}

	@Override
	public String getName() {
		return "customized";
	}
}
