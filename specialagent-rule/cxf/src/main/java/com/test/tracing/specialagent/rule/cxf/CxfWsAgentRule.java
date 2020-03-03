package com.test.tracing.specialagent.rule.cxf;

import static net.bytebuddy.matcher.ElementMatchers.hasSuperType;
import static net.bytebuddy.matcher.ElementMatchers.named;

import java.util.Arrays;

import io.opentracing.contrib.specialagent.AgentRule;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.agent.builder.AgentBuilder.Transformer;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.utility.JavaModule;

public class CxfWsAgentRule extends AgentRule {
	@Override
	public Iterable<? extends AgentBuilder> buildAgent(final AgentBuilder builder) throws Exception {
		return Arrays.asList(builder.type(hasSuperType(named("org.apache.cxf.frontend.ClientFactoryBean")))
				.transform(new Transformer() {
					@Override
					public Builder<?> transform(final Builder<?> builder, final TypeDescription typeDescription,
							final ClassLoader classLoader, final JavaModule module) {
						return builder.visit(Advice.to(CxfWsClientAdvice.class).on(named("create")));
					}
				}), builder.type(hasSuperType(named("org.apache.cxf.frontend.ServerFactoryBean")))
						.transform(new Transformer() {
							@Override
							public Builder<?> transform(final Builder<?> builder, final TypeDescription typeDescription,
									final ClassLoader classLoader, final JavaModule module) {
								return builder.visit(Advice.to(CxfWsServerAdvice.class).on(named("create")));
							}
						}));
	}

	public static class CxfWsClientAdvice {
		@Advice.OnMethodEnter
		public static void enter(final @Advice.Origin String origin, final @Advice.This Object thiz) {
			if (isEnabled("CxfWsAgentRule", origin)) {
				CxfAgentIntercept.addClientTracingFeature(thiz);
			}
		}
	}

	public static class CxfWsServerAdvice {
		@Advice.OnMethodEnter
		public static void enter(final @Advice.Origin String origin, final @Advice.This Object thiz) {
			if (isEnabled("CxfWsAgentRule", origin)) {
				CxfAgentIntercept.addServerTracingFeauture(thiz);
			}
		}
	}
}