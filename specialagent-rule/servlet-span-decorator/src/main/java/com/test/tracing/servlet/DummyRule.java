package com.test.tracing.servlet;

import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.isPublic;

import java.util.Arrays;

import io.opentracing.contrib.specialagent.AgentRule;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.agent.builder.AgentBuilder.Transformer;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.utility.JavaModule;

public class DummyRule extends AgentRule {

	@Override
	public Iterable<? extends AgentBuilder> buildAgent(final AgentBuilder builder) throws Exception {
		return Arrays.asList(builder.type(named("org.eclipse.jetty.server.Server")).transform(new Transformer() {
			@Override
			public Builder<?> transform(final Builder<?> builder, final TypeDescription typeDescription,
					final ClassLoader classLoader, final JavaModule module) {
				return builder.visit(Advice.to(JettyServerAdvice.class).on(named("start").and(isPublic())));
			}
		}), builder.type(named("io.undertow.Undertow")).transform(new Transformer() {
			@Override
			public Builder<?> transform(final Builder<?> builder, final TypeDescription typeDescription,
					final ClassLoader classLoader, final JavaModule module) {
				return builder.visit(Advice.to(UndertowServerAdvice.class).on(named("start")));
			}
		}));
	}

	public static class JettyServerAdvice {
		@Advice.OnMethodEnter
		public static void enter(final @Advice.Origin String origin, final @Advice.This Object thiz) {
			if (isEnabled("DummyRule", origin)) {
				loadClass();
			}
		}
	}

	public static class UndertowServerAdvice {
		@Advice.OnMethodEnter
		public static void enter(final @Advice.Origin String origin, final @Advice.This Object thiz) {
			if (isEnabled("DummyRule", origin)) {
				loadClass();
			}
		}
	}

	private static void loadClass() {
		CustomizedSpanDecorator.class.getName();
	}
}
