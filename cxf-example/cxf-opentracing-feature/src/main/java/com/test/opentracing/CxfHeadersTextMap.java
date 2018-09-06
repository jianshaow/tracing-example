package com.test.opentracing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import io.opentracing.propagation.TextMap;

public class CxfHeadersTextMap implements TextMap {

    private Map<String, List<String>> headers;

    public CxfHeadersTextMap(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    @Override
    public Iterator<Entry<String, String>> iterator() {
        return headers.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, this::getFirstValueOrEmpty))
                .entrySet().iterator();
    }

    @Override
    public void put(String key, String value) {
        List<String> values = headers.get(key);
        if (values == null) {
            values = new ArrayList<>(1);
            headers.put(key, values);
        }

        values.add(value);
    }

    private String getFirstValueOrEmpty(Map.Entry<String, List<String>> entry) {
        final List<String> values = entry.getValue();

        if (values == null || values.isEmpty()) {
            return "";
        }

        final String value = values.get(0);
        return (value != null) ? value : "";
    }
}
