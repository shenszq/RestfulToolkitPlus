package com.shenszq.restful.method;

import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public enum HttpMethod {
    GET,
    POST,
    PUT,
    DELETE,
    PATCH,
    HEAD,
    OPTIONS,
    TRACE,
    CONNECT;

    @Nullable
    public static HttpMethod getByRequestMethod(@Nullable String method) {
        if (method == null || method.isBlank()) {
            return null;
        }

        String normalized = method.trim();
        int separator = normalized.lastIndexOf('.');
        if (separator >= 0 && separator + 1 < normalized.length()) {
            normalized = normalized.substring(separator + 1);
        }

        try {
            return HttpMethod.valueOf(normalized.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ignore) {
            return null;
        }
    }
}
