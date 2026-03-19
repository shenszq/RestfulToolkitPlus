package com.zhaow.restful.common.resolver;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.AnnotatedElementsSearch;
import com.zhaow.restful.annotations.SpringControllerAnnotation;
import com.zhaow.restful.common.spring.RequestMappingAnnotationHelper;
import com.zhaow.restful.method.RequestPath;
import com.zhaow.restful.navigation.action.RestServiceItem;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class SpringResolver extends BaseServiceResolver {
    public SpringResolver(@NotNull Module module) {
        myModule = module;
    }

    public SpringResolver(@NotNull Project project) {
        myProject = project;
    }

    @Override
    @NotNull
    protected List<RestServiceItem> getRestServiceItemList(@NotNull Project project, @NotNull GlobalSearchScope scope) {
        List<RestServiceItem> items = new ArrayList<>();
        Set<String> visitedClasses = new LinkedHashSet<>();

        for (SpringControllerAnnotation controllerAnnotation : SpringControllerAnnotation.values()) {
            PsiClass annotationClass = JavaPsiFacade.getInstance(project)
                .findClass(controllerAnnotation.getQualifiedName(), GlobalSearchScope.allScope(project));
            if (annotationClass == null) {
                continue;
            }

            AnnotatedElementsSearch.searchPsiClasses(annotationClass, scope).forEach(psiClass -> {
                String key = psiClass.getQualifiedName() != null
                    ? psiClass.getQualifiedName()
                    : psiClass.getContainingFile().getVirtualFile() + ":" + psiClass.getTextOffset();
                if (visitedClasses.add(key)) {
                    items.addAll(getServiceItemList(psiClass));
                }
                return true;
            });
        }

        return items;
    }

    @NotNull
    private List<RestServiceItem> getServiceItemList(@NotNull PsiClass psiClass) {
        List<RequestPath> classRequestPaths = RequestMappingAnnotationHelper.getRequestPaths(psiClass);
        List<RestServiceItem> items = new ArrayList<>();

        for (PsiMethod psiMethod : psiClass.getMethods()) {
            RequestPath[] methodRequestPaths = RequestMappingAnnotationHelper.getRequestPaths(psiMethod);
            if (methodRequestPaths.length == 0) {
                continue;
            }

            for (RequestPath classRequestPath : classRequestPaths) {
                for (RequestPath methodRequestPath : methodRequestPaths) {
                    items.add(createRestServiceItem(psiMethod, classRequestPath.getPath(), methodRequestPath));
                }
            }
        }

        return items;
    }
}
