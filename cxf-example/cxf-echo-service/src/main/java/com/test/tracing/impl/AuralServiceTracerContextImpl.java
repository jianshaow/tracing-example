package com.test.tracing.impl;

import org.apache.cxf.tracing.TracerContext;

import jakarta.ws.rs.core.Context;

public class AuralServiceTracerContextImpl extends AuralServiceImpl {

    @Context
    private TracerContext tracerContext;

    @Override
    public void beforeCallMindService(String msg) {
        tracerContext.timeline("recall in mind with " + msg + "...");
    }
}
