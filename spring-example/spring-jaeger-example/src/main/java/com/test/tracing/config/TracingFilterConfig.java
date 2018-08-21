/*------------------------------------------------------------------------------
 * COPYRIGHT Ericsson 2018
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *----------------------------------------------------------------------------*/
package com.test.tracing.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.opentracing.Tracer;
import io.opentracing.contrib.web.servlet.filter.TracingFilter;

@Configuration
public class TracingFilterConfig {

    @Autowired
    private Tracer tracer;

    @Bean
    public TracingFilter tracingFilter() {
        return new TracingFilter(tracer);
    }
}