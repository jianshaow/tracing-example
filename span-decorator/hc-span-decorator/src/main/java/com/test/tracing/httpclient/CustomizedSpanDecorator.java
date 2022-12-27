package com.test.tracing.httpclient;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import io.opentracing.Span;
import io.opentracing.contrib.specialagent.rule.apache.httpclient.ApacheClientSpanDecorator;

public class CustomizedSpanDecorator implements ApacheClientSpanDecorator {

  @Override
  public void onRequest(HttpRequest request, HttpHost httpHost, Span span) {
    span.setTag("customized.tag", "we tag what we want");
  }

  @Override
  public void onResponse(HttpResponse response, Span span) {
    span.setTag("customized.tag", "we tag what we want");
  }

  @Override
  public void onError(Throwable thrown, Span span) {
    span.setTag("customized.tag", "we tag what we want");
  }
}
