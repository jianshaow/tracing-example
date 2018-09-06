package com.test.opentracing;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.ws.rs.core.MultivaluedMap;

import io.opentracing.propagation.TextMap;

public class JaxRsHeadersTextMap implements TextMap {

    private MultivaluedMap<String, String> headers;

    public JaxRsHeadersTextMap(MultivaluedMap<String, String> headers) {
        this.headers = headers;
    }

    @Override
    public Iterator<Entry<String, String>> iterator() {
        return headers.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, TextMapUtils::getFirstValueOrEmpty)).entrySet().iterator();
    }

    @Override
    public void put(String key, String value) {
        headers.add(key, value);
    }
}
