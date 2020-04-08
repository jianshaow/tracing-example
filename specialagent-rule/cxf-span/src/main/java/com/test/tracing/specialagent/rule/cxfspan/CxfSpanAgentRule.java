package com.test.tracing.specialagent.rule.cxfspan;

import static net.bytebuddy.matcher.ElementMatchers.hasSuperType;
import static net.bytebuddy.matcher.ElementMatchers.named;
import io.opentracing.contrib.specialagent.AgentRule;
import io.opentracing.tag.Tags;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.agent.builder.AgentBuilder.Transformer;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.implementation.bytecode.assign.Assigner.Typing;
import net.bytebuddy.utility.JavaModule;

public class CxfSpanAgentRule extends AgentRule {
  @Override
  public AgentBuilder buildAgentChainedGlobal1(final AgentBuilder builder) {
    return builder
        .type(hasSuperType(
            named("org.apache.cxf.tracing.opentracing.AbstractOpenTracingClientProvider")))
        .transform(new Transformer() {
          @Override
          public Builder<?> transform(final Builder<?> builder,
              final TypeDescription typeDescription, final ClassLoader classLoader,
              final JavaModule module) {
            return builder.visit(Advice.to(CxfSpanClientAdvice.class).on(named("stopTraceSpan")));
          }
        });
  }

  @Override
  public AgentBuilder buildAgentChainedGlobal2(final AgentBuilder builder) {
    return builder
        .type(hasSuperType(
            named("org.apache.cxf.tracing.opentracing.AbstractOpenTracingProvider")))
        .transform(new Transformer() {
          @Override
          public Builder<?> transform(final Builder<?> builder,
              final TypeDescription typeDescription, final ClassLoader classLoader,
              final JavaModule module) {
            return builder
                .visit(Advice.to(CxfSpanServerAdvice.class).on(named("stopTraceSpan")));
          }
        });
  }

  public static class CxfSpanClientAdvice {
    @Advice.OnMethodEnter
    public static void enter(final @Advice.Origin String origin,
        @Advice.Argument(value = 0, readOnly = false, typing = Typing.DYNAMIC) Object request) {
      if (isAllowed("CxfRsAgentRule", origin)) {
        CxfSpanAgentIntercept.stopTracingSpan(request, "cxf-client", Tags.SPAN_KIND_CLIENT);
      }
    }
  }

  public static class CxfSpanServerAdvice {
    @Advice.OnMethodEnter
    public static void enter(final @Advice.Origin String origin,
        @Advice.Argument(value = 3, readOnly = false, typing = Typing.DYNAMIC) Object request) {
      if (isAllowed("CxfRsAgentRule", origin)) {
        CxfSpanAgentIntercept.stopTracingSpan(request, "cxf-server", Tags.SPAN_KIND_SERVER);
      }
    }
  }
}
