package com.zhaow.restful.common;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

public final class UrlPatternUtils {
    private static final Pattern SCHEME_HOST = Pattern.compile("^(?:[a-zA-Z][a-zA-Z0-9+.-]*://)(?:[^/]+)");
    private static final Pattern LOCALHOST_HOST = Pattern.compile("^localhost(?::\\d+)?(?=/|$)");
    private static final Pattern DOMAIN_HOST = Pattern.compile("^(?:\\d{1,3}(?:\\.\\d{1,3}){3}|(?:[A-Za-z0-9-]+(?:\\.[A-Za-z0-9-]+)+))(?::\\d+)?(?=/|$)");

    private UrlPatternUtils() {
    }

    @NotNull
    public static String normalizeUserInput(@Nullable String value) {
        if (value == null) {
            return "";
        }

        String normalized = value.trim().replace('\\', '/');
        normalized = stripQuotes(normalized);
        normalized = stripQueryAndFragment(normalized);
        normalized = stripHost(normalized);
        normalized = normalized.replaceAll("/+", "/");

        if (normalized.isEmpty()) {
            return "/";
        }
        if (!normalized.startsWith("/")) {
            normalized = "/" + normalized;
        }
        return normalized;
    }

    public static boolean matches(@NotNull String endpointPath, @NotNull String userPattern) {
        String endpoint = normalizeUserInput(endpointPath);
        String input = normalizeUserInput(userPattern);

        if ("/".equals(input)) {
            return true;
        }

        String endpointLowerCase = endpoint.toLowerCase(Locale.ROOT);
        String endpointRegex = toRegex(endpoint);
        for (String candidate : buildCandidatePaths(input)) {
            String candidateLowerCase = candidate.toLowerCase(Locale.ROOT);
            if (endpointLowerCase.equals(candidateLowerCase) || endpointLowerCase.contains(candidateLowerCase)) {
                return true;
            }

            if (endpointLowerCase.startsWith(appendSlash(candidateLowerCase))) {
                return true;
            }

            if (candidate.matches(endpointRegex)) {
                return true;
            }
        }
        return false;
    }

    @NotNull
    private static String stripQuotes(@NotNull String value) {
        if (value.length() >= 2 && value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }

    @NotNull
    private static String stripQueryAndFragment(@NotNull String value) {
        int queryIndex = value.indexOf('?');
        int hashIndex = value.indexOf('#');
        int cutIndex = -1;
        if (queryIndex >= 0) {
            cutIndex = queryIndex;
        }
        if (hashIndex >= 0 && (cutIndex < 0 || hashIndex < cutIndex)) {
            cutIndex = hashIndex;
        }
        return cutIndex >= 0 ? value.substring(0, cutIndex) : value;
    }

    @NotNull
    private static String stripHost(@NotNull String value) {
        String normalized = SCHEME_HOST.matcher(value).replaceFirst("");
        if (!normalized.equals(value)) {
            return normalized.isEmpty() ? "/" : normalized;
        }

        normalized = LOCALHOST_HOST.matcher(value).replaceFirst("");
        if (!normalized.equals(value)) {
            return normalized.isEmpty() ? "/" : normalized;
        }

        normalized = DOMAIN_HOST.matcher(value).replaceFirst("");
        if (!normalized.equals(value)) {
            return normalized.isEmpty() ? "/" : normalized;
        }

        return value;
    }

    @NotNull
    private static String toRegex(@NotNull String endpoint) {
        StringBuilder regex = new StringBuilder("^");
        for (int index = 0; index < endpoint.length(); index++) {
            char current = endpoint.charAt(index);
            if (current == '{') {
                int end = endpoint.indexOf('}', index);
                if (end < 0) {
                    regex.append("\\{");
                    continue;
                }

                String variable = endpoint.substring(index + 1, end);
                int separator = variable.indexOf(':');
                if (separator >= 0 && separator + 1 < variable.length()) {
                    regex.append('(').append(variable.substring(separator + 1)).append(')');
                } else {
                    regex.append("[^/]+");
                }
                index = end;
                continue;
            }

            if ("\\.[]{}()+-*?^$|".indexOf(current) >= 0) {
                regex.append('\\');
            }
            regex.append(current);
        }
        regex.append('$');
        return regex.toString();
    }

    @NotNull
    private static List<String> buildCandidatePaths(@NotNull String input) {
        Set<String> candidates = new LinkedHashSet<>();
        candidates.add(input);

        String trimmed = input.startsWith("/") ? input.substring(1) : input;
        if (trimmed.isEmpty()) {
            return List.of("/");
        }

        String[] segments = trimmed.split("/");
        for (int index = 1; index < segments.length; index++) {
            String suffix = joinNonEmptySegments(segments, index);
            if (!suffix.isEmpty()) {
                candidates.add("/" + suffix);
            }
        }

        return new ArrayList<>(candidates);
    }

    @NotNull
    private static String joinNonEmptySegments(@NotNull String[] segments, int startIndex) {
        StringBuilder result = new StringBuilder();
        for (int index = startIndex; index < segments.length; index++) {
            if (segments[index].isEmpty()) {
                continue;
            }
            if (result.length() > 0) {
                result.append('/');
            }
            result.append(segments[index]);
        }
        return result.toString();
    }

    @NotNull
    private static String appendSlash(@NotNull String path) {
        return path.endsWith("/") ? path : path + "/";
    }
}
