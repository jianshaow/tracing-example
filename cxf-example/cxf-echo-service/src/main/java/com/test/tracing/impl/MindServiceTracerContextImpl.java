package com.test.tracing.impl;

import org.apache.cxf.tracing.TracerContext;

import jakarta.ws.rs.core.Context;

public class MindServiceTracerContextImpl extends MindServiceImpl {

    @Context
    private TracerContext tracerContext;

    @Override
    protected void beforeCallMemory() {
        tracerContext.timeline("asking memory...");
        super.beforeCallMemory();
    }
}
