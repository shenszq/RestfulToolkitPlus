package com.zhaow.restful.common.resolver;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.zhaow.restful.method.RequestPath;
import com.zhaow.restful.navigation.action.RestServiceItem;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class BaseServiceResolver implements ServiceResolver {
    protected Module myModule;
    protected Project myProject;

    @Override
    @NotNull
    public List<RestServiceItem> findAllSupportedServiceItemsInModule() {
        if (myModule == null) {
            return List.of();
        }
        GlobalSearchScope scope = GlobalSearchScope.moduleScope(myModule);
        return getRestServiceItemList(myModule.getProject(), scope);
    }

    @Override
    @NotNull
    public List<RestServiceItem> findAllSupportedServiceItemsInProject() {
        if (myProject == null && myModule != null) {
            myProject = myModule.getProject();
        }
        if (myProject == null) {
            return List.of();
        }
        GlobalSearchScope scope = GlobalSearchScope.projectScope(myProject);
        return getRestServiceItemList(myProject, scope);
    }

    @NotNull
    protected abstract List<RestServiceItem> getRestServiceItemList(@NotNull Project project, @NotNull GlobalSearchScope scope);

    @NotNull
    protected RestServiceItem createRestServiceItem(@NotNull PsiElement psiElement,
                                                    @NotNull String classUriPath,
                                                    @NotNull RequestPath requestPath) {
        String classPath = classUriPath;
        if (!classPath.startsWith("/")) {
            classPath = "/" + classPath;
        }
        if (!classPath.endsWith("/")) {
            classPath = classPath + "/";
        }

        String methodPath = requestPath.getPath();
        if (methodPath.startsWith("/")) {
            methodPath = methodPath.substring(1);
        }

        RestServiceItem item = new RestServiceItem(psiElement, requestPath.getMethod(), classPath + methodPath);
        if (myModule != null) {
            item.setModule(myModule);
        }
        return item;
    }
}
