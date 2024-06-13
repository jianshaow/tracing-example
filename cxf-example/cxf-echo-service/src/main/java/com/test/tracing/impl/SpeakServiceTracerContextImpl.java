package com.test.tracing.impl;

import jakarta.ws.rs.core.Context;

import org.apache.cxf.tracing.TracerContext;

public class SpeakServiceTracerContextImpl extends SpeakServiceImpl {

    @Context
    private TracerContext tracerContext;

    @Override
    public void afterCallMindService() {
        tracerContext.timeline("respond in mind...");
    }
}
