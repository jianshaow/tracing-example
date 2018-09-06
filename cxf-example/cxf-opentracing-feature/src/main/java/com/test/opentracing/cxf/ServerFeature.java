package com.test.opentracing.cxf;

import org.apache.cxf.Bus;
import org.apache.cxf.annotations.Provider;
import org.apache.cxf.annotations.Provider.Scope;
import org.apache.cxf.annotations.Provider.Type;
import org.apache.cxf.common.injection.NoJSR250Annotations;
import org.apache.cxf.feature.AbstractFeature;
import org.apache.cxf.interceptor.InterceptorProvider;

import io.opentracing.Tracer;

@NoJSR250Annotations
@Provider(value = Type.Feature, scope = Scope.Server)
public class ServerFeature extends AbstractFeature {
    private ServerStartInterceptor in;
    private ServerStopInterceptor out;

    public ServerFeature(final Tracer tracer) {
        in = new ServerStartInterceptor(tracer);
        out = new ServerStopInterceptor(tracer);
    }

    @Override
    protected void initializeProvider(InterceptorProvider provider, @SuppressWarnings("unused") Bus bus) {
        provider.getInInterceptors().add(in);
        provider.getInFaultInterceptors().add(in);

        provider.getOutInterceptors().add(out);
        provider.getOutFaultInterceptors().add(out);
    }
}
