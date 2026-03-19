package com.zhaow.restful.annotations;

import org.jetbrains.annotations.Nullable;

public enum JaxrsHttpMethodAnnotation {
    JAVAX_GET("javax.ws.rs.GET", "GET"),
    JAVAX_POST("javax.ws.rs.POST", "POST"),
    JAVAX_PUT("javax.ws.rs.PUT", "PUT"),
    JAVAX_DELETE("javax.ws.rs.DELETE", "DELETE"),
    JAVAX_HEAD("javax.ws.rs.HEAD", "HEAD"),
    JAVAX_OPTIONS("javax.ws.rs.OPTIONS", "OPTIONS"),
    JAVAX_PATCH("javax.ws.rs.PATCH", "PATCH"),
    JAKARTA_GET("jakarta.ws.rs.GET", "GET"),
    JAKARTA_POST("jakarta.ws.rs.POST", "POST"),
    JAKARTA_PUT("jakarta.ws.rs.PUT", "PUT"),
    JAKARTA_DELETE("jakarta.ws.rs.DELETE", "DELETE"),
    JAKARTA_HEAD("jakarta.ws.rs.HEAD", "HEAD"),
    JAKARTA_OPTIONS("jakarta.ws.rs.OPTIONS", "OPTIONS"),
    JAKARTA_PATCH("jakarta.ws.rs.PATCH", "PATCH");

    private final String qualifiedName;
    private final String methodName;

    JaxrsHttpMethodAnnotation(String qualifiedName, String methodName) {
        this.qualifiedName = qualifiedName;
        this.methodName = methodName;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public String methodName() {
        return methodName;
    }

    @Nullable
    public static JaxrsHttpMethodAnnotation getByQualifiedName(String value) {
        for (JaxrsHttpMethodAnnotation annotation : values()) {
            if (annotation.qualifiedName.equals(value)) {
                return annotation;
            }
        }
        return null;
    }
}
