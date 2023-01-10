package com.test.tracing.extension;

import java.util.HashMap;
import java.util.Map;

import com.google.auto.service.AutoService;
import com.test.tracing.extension.exporter.CustomizedExporter;
import com.test.tracing.extension.processor.CustomizedProcessor;

import io.opentelemetry.sdk.autoconfigure.spi.AutoConfigurationCustomizer;
import io.opentelemetry.sdk.autoconfigure.spi.AutoConfigurationCustomizerProvider;
import io.opentelemetry.sdk.autoconfigure.spi.ConfigProperties;
import io.opentelemetry.sdk.trace.SdkTracerProviderBuilder;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;

@AutoService(AutoConfigurationCustomizerProvider.class)
public class CustomizedAutoConfigurationCustomizerProvider implements AutoConfigurationCustomizerProvider {

	@Override
	public void customize(AutoConfigurationCustomizer autoConfiguration) {
		autoConfiguration.addTracerProviderCustomizer(this::configureSdkTracerProvider)
				.addPropertiesSupplier(this::getDefaultProperties);
	}

	private SdkTracerProviderBuilder configureSdkTracerProvider(SdkTracerProviderBuilder tracerProvider,
			ConfigProperties config) {
		return tracerProvider.addSpanProcessor(new CustomizedProcessor())
				.addSpanProcessor(SimpleSpanProcessor.create(new CustomizedExporter()));
	}

	private Map<String, String> getDefaultProperties() {
		Map<String, String> properties = new HashMap<>();
		properties.put("otel.exporter.otlp.endpoint", "http://backend:8080");
		properties.put("otel.exporter.otlp.insecure", "true");
		properties.put("otel.config.max.attrs", "16");
		properties.put("otel.traces.sampler", "customized");
		return properties;
	}
}
