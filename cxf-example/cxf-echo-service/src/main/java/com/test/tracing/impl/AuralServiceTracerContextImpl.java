package com.test.tracing.impl;

import javax.ws.rs.core.Context;

import org.apache.cxf.tracing.TracerContext;

public class AuralServiceTracerContextImpl extends AuralServiceImpl {

    @Context
    private TracerContext tracerContext;

    @Override
    public void beforeCallMindService(String msg) {
        tracerContext.timeline("recall in mind with " + msg + "...");
    }
}
