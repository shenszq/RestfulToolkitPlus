package com.zhaow.restful.common;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiArrayInitializerMemberValue;
import com.intellij.psi.PsiLiteralExpression;
import com.intellij.psi.PsiReferenceExpression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class PsiAnnotationHelper {
    private PsiAnnotationHelper() {
    }

    @NotNull
    public static List<String> getAnnotationAttributeValues(@Nullable PsiAnnotation annotation, String attr) {
        if (annotation == null) {
            return List.of();
        }

        PsiAnnotationMemberValue value = annotation.findDeclaredAttributeValue(attr);
        if (value == null) {
            return List.of();
        }

        List<String> values = new ArrayList<>();
        if (value instanceof PsiReferenceExpression referenceExpression) {
            values.add(referenceExpression.getText());
        } else if (value instanceof PsiLiteralExpression literalExpression) {
            Object literalValue = literalExpression.getValue();
            if (literalValue != null) {
                values.add(literalValue.toString());
            }
        } else if (value instanceof PsiArrayInitializerMemberValue arrayValue) {
            for (PsiAnnotationMemberValue initializer : arrayValue.getInitializers()) {
                values.add(initializer.getText().replace("\"", ""));
            }
        }

        return values;
    }

    @Nullable
    public static String getAnnotationAttributeValue(@Nullable PsiAnnotation annotation, String attr) {
        List<String> values = getAnnotationAttributeValues(annotation, attr);
        return values.isEmpty() ? null : values.get(0);
    }
}
