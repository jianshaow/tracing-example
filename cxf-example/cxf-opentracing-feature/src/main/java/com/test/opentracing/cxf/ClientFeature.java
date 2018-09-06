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
@Provider(value = Type.Feature, scope = Scope.Client)
public class ClientFeature extends AbstractFeature {
    private ClientStopInterceptor in;
    private ClientStartInterceptor out;

    public ClientFeature(final Tracer tracer) {
        in = new ClientStopInterceptor(tracer);
        out = new ClientStartInterceptor(tracer);
    }

    @Override
    protected void initializeProvider(InterceptorProvider provider, @SuppressWarnings("unused") Bus bus) {
        provider.getInInterceptors().add(in);
        provider.getOutInterceptors().add(out);
    }
}
