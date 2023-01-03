package com.test.tracing.impl;

import javax.ws.rs.core.Context;

import org.apache.cxf.tracing.TracerContext;

public class SpeakServiceTracerContextImpl extends SpeakServiceImpl {

    @Context
    private TracerContext tracerContext;

    @Override
    public void afterCallMindService() {
        tracerContext.timeline("respond in mind...");
    }
}
