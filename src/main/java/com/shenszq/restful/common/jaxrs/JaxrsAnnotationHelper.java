package com.shenszq.restful.common.jaxrs;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.shenszq.restful.annotations.JaxrsHttpMethodAnnotation;
import com.shenszq.restful.annotations.JaxrsPathAnnotation;
import com.shenszq.restful.common.PsiAnnotationHelper;
import com.shenszq.restful.method.RequestPath;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class JaxrsAnnotationHelper {
    private JaxrsAnnotationHelper() {
    }

    @NotNull
    public static RequestPath[] getRequestPaths(@NotNull PsiMethod psiMethod) {
        String path = getPathValue(findPathAnnotation(psiMethod.getAnnotations()));
        if (path == null || path.isBlank()) {
            path = psiMethod.getName();
        }

        List<RequestPath> result = new ArrayList<>();
        for (PsiAnnotation annotation : psiMethod.getAnnotations()) {
            JaxrsHttpMethodAnnotation httpMethodAnnotation =
                JaxrsHttpMethodAnnotation.getByQualifiedName(annotation.getQualifiedName());
            if (httpMethodAnnotation != null) {
                result.add(new RequestPath(path, httpMethodAnnotation.methodName()));
            }
        }
        return result.toArray(RequestPath[]::new);
    }

    @NotNull
    public static String getClassUriPath(@NotNull PsiClass psiClass) {
        String value = getPathValue(findPathAnnotation(psiClass.getAnnotations()));
        return value == null ? "" : value;
    }

    @Nullable
    private static PsiAnnotation findPathAnnotation(PsiAnnotation[] annotations) {
        for (PsiAnnotation annotation : annotations) {
            for (JaxrsPathAnnotation pathAnnotation : JaxrsPathAnnotation.values()) {
                if (pathAnnotation.getQualifiedName().equals(annotation.getQualifiedName())) {
                    return annotation;
                }
            }
        }
        return null;
    }

    @Nullable
    private static String getPathValue(@Nullable PsiAnnotation annotation) {
        return PsiAnnotationHelper.getAnnotationAttributeValue(annotation, "value");
    }
}
