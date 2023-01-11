package com.test.tracing.impl;

import org.apache.cxf.tracing.TracerContext;

import jakarta.ws.rs.core.Context;

public class SpeakServiceTracerContextImpl extends SpeakServiceImpl {

    @Context
    private TracerContext tracerContext;

    @Override
    public void afterCallMindService() {
        tracerContext.timeline("respond in mind...");
    }
}
