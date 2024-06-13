package com.test.tracing.impl;

import jakarta.ws.rs.core.Context;

import org.apache.cxf.tracing.TracerContext;

public class MindServiceTracerContextImpl extends MindServiceImpl {

    @Context
    private TracerContext tracerContext;

    @Override
    protected void beforeCallMemory() {
        tracerContext.timeline("asking memory...");
        super.beforeCallMemory();
    }
}
