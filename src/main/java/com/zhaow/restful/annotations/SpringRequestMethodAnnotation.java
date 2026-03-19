package com.zhaow.restful.annotations;

import org.jetbrains.annotations.Nullable;

public enum SpringRequestMethodAnnotation {
    REQUEST_MAPPING("org.springframework.web.bind.annotation.RequestMapping", null),
    GET_MAPPING("org.springframework.web.bind.annotation.GetMapping", "GET"),
    POST_MAPPING("org.springframework.web.bind.annotation.PostMapping", "POST"),
    PUT_MAPPING("org.springframework.web.bind.annotation.PutMapping", "PUT"),
    DELETE_MAPPING("org.springframework.web.bind.annotation.DeleteMapping", "DELETE"),
    PATCH_MAPPING("org.springframework.web.bind.annotation.PatchMapping", "PATCH");

    private final String qualifiedName;
    private final String methodName;

    SpringRequestMethodAnnotation(String qualifiedName, String methodName) {
        this.qualifiedName = qualifiedName;
        this.methodName = methodName;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    @Nullable
    public String methodName() {
        return methodName;
    }

    public String getShortName() {
        int index = qualifiedName.lastIndexOf('.');
        return index >= 0 ? qualifiedName.substring(index + 1) : qualifiedName;
    }

    @Nullable
    public static SpringRequestMethodAnnotation getByQualifiedName(String value) {
        for (SpringRequestMethodAnnotation annotation : values()) {
            if (annotation.qualifiedName.equals(value)) {
                return annotation;
            }
        }
        return null;
    }
}
