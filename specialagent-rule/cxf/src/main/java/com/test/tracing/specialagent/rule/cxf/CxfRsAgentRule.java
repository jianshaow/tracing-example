package com.test.tracing.specialagent.rule.cxf;

import static net.bytebuddy.matcher.ElementMatchers.hasSuperType;
import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.nameStartsWith;

import java.util.Arrays;

import io.opentracing.contrib.specialagent.AgentRule;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.agent.builder.AgentBuilder.Transformer;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.utility.JavaModule;

public class CxfRsAgentRule extends AgentRule {
	@Override
	public Iterable<? extends AgentBuilder> buildAgent(final AgentBuilder builder) throws Exception {
		return Arrays.asList(builder.type(hasSuperType(named("org.apache.cxf.jaxrs.client.JAXRSClientFactoryBean")))
				.transform(new Transformer() {
					@Override
					public Builder<?> transform(final Builder<?> builder, final TypeDescription typeDescription,
							final ClassLoader classLoader, final JavaModule module) {
						return builder.visit(Advice.to(CxfRsClientAdvice.class).on(nameStartsWith("create")));
					}
				}), builder.type(hasSuperType(named("org.apache.cxf.jaxrs.JAXRSServerFactoryBean")))
						.transform(new Transformer() {
							@Override
							public Builder<?> transform(final Builder<?> builder, final TypeDescription typeDescription,
									final ClassLoader classLoader, final JavaModule module) {
								return builder.visit(Advice.to(CxfRsServerAdvice.class).on(named("create")));
							}
						}));
	}

	public static class CxfRsClientAdvice {
		@Advice.OnMethodEnter
		public static void enter(final @Advice.Origin String origin, final @Advice.This Object thiz) {
			if (isEnabled("CxfAgentRule", origin)) {
				CxfAgentIntercept.addRsClientTracingFeature(thiz);
			}
		}
	}

	public static class CxfRsServerAdvice {
		@Advice.OnMethodEnter
		public static void enter(final @Advice.Origin String origin, final @Advice.This Object thiz) {
			if (isEnabled("CxfAgentRule", origin)) {
				CxfAgentIntercept.addRsServerTracingFeauture(thiz);
			}
		}
	}
}