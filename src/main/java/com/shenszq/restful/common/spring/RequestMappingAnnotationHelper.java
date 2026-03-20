package com.shenszq.restful.common.spring;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.shenszq.restful.annotations.SpringRequestMethodAnnotation;
import com.shenszq.restful.common.PsiAnnotationHelper;
import com.shenszq.restful.method.RequestPath;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class RequestMappingAnnotationHelper {
    private RequestMappingAnnotationHelper() {
    }

    @NotNull
    public static List<RequestPath> getRequestPaths(@NotNull PsiClass psiClass) {
        PsiAnnotation requestMappingAnnotation = findMappingAnnotation(psiClass.getAnnotations());
        if (requestMappingAnnotation != null) {
            List<RequestPath> requestMappings = getRequestMappings(requestMappingAnnotation, "/");
            if (!requestMappings.isEmpty()) {
                return requestMappings;
            }
        }

        PsiClass superClass = psiClass.getSuperClass();
        if (superClass != null && !"java.lang.Object".equals(superClass.getQualifiedName())) {
            return getRequestPaths(superClass);
        }

        return List.of(new RequestPath("/", null));
    }

    @NotNull
    public static RequestPath[] getRequestPaths(@NotNull PsiMethod psiMethod) {
        List<RequestPath> list = new ArrayList<>();
        for (PsiAnnotation annotation : psiMethod.getAnnotations()) {
            SpringRequestMethodAnnotation requestMethodAnnotation =
                SpringRequestMethodAnnotation.getByQualifiedName(annotation.getQualifiedName());
            if (requestMethodAnnotation == null) {
                continue;
            }

            list.addAll(getRequestMappings(annotation, "/"));
        }
        return list.toArray(RequestPath[]::new);
    }

    @NotNull
    private static List<RequestPath> getRequestMappings(@NotNull PsiAnnotation annotation, @NotNull String defaultValue) {
        SpringRequestMethodAnnotation requestAnnotation =
            SpringRequestMethodAnnotation.getByQualifiedName(annotation.getQualifiedName());
        if (requestAnnotation == null) {
            return List.of();
        }

        List<String> methodList = requestAnnotation.methodName() == null
            ? PsiAnnotationHelper.getAnnotationAttributeValues(annotation, "method")
            : List.of(requestAnnotation.methodName());

        List<String> pathList = new ArrayList<>(PsiAnnotationHelper.getAnnotationAttributeValues(annotation, "value"));
        if (pathList.isEmpty()) {
            pathList.addAll(PsiAnnotationHelper.getAnnotationAttributeValues(annotation, "path"));
        }
        if (pathList.isEmpty()) {
            pathList.add(defaultValue);
        }

        List<RequestPath> result = new ArrayList<>();
        if (methodList.isEmpty()) {
            for (String path : pathList) {
                result.add(new RequestPath(path, null));
            }
            return result;
        }

        for (String method : methodList) {
            for (String path : pathList) {
                result.add(new RequestPath(path, method));
            }
        }
        return result;
    }

    private static PsiAnnotation findMappingAnnotation(PsiAnnotation[] annotations) {
        for (PsiAnnotation annotation : annotations) {
            if (SpringRequestMethodAnnotation.getByQualifiedName(annotation.getQualifiedName()) != null) {
                return annotation;
            }
        }
        return null;
    }
}
