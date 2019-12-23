package com.test.opentracing;

import java.util.List;
import java.util.Map;

public class TextMapUtils {

    public static String getFirstValueOrEmpty(Map.Entry<String, List<String>> entry) {
        final List<String> values = entry.getValue();

        if (values == null || values.isEmpty()) {
            return "";
        }

        final String value = values.get(0);
        return (value != null) ? value : "";
    }
}