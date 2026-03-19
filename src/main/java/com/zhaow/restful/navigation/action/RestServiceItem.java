package com.zhaow.restful.navigation.action;

import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.module.Module;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.zhaow.restful.common.ToolkitIcons;
import com.zhaow.restful.method.HttpMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;

public final class RestServiceItem implements NavigationItem {
    private final PsiElement psiElement;
    private final String url;
    private final HttpMethod method;
    private Module module;

    public RestServiceItem(@NotNull PsiElement psiElement, @Nullable String requestMethod, @NotNull String url) {
        this.psiElement = psiElement;
        this.url = url;
        this.method = HttpMethod.getByRequestMethod(requestMethod);
    }

    @Override
    @Nullable
    public String getName() {
        return url;
    }

    @Override
    @Nullable
    public ItemPresentation getPresentation() {
        return new RestServiceItemPresentation();
    }

    @Override
    public void navigate(boolean requestFocus) {
        PsiElement navigationElement = psiElement.getNavigationElement();
        if (navigationElement instanceof Navigatable navigatable && navigatable.canNavigate()) {
            navigatable.navigate(requestFocus);
        }
    }

    @Override
    public boolean canNavigate() {
        PsiElement navigationElement = psiElement.getNavigationElement();
        return navigationElement instanceof Navigatable navigatable && navigatable.canNavigate();
    }

    @Override
    public boolean canNavigateToSource() {
        return canNavigate();
    }

    @Nullable
    public HttpMethod getMethod() {
        return method;
    }

    public void setModule(@NotNull Module module) {
        this.module = module;
    }

    @NotNull
    public String getDedupKey() {
        PsiElement navigationElement = psiElement.getNavigationElement();
        PsiElement baseElement = navigationElement != null ? navigationElement : psiElement;
        return url + "|" + method + "|" + baseElement.getContainingFile().getVirtualFile() + "|" + baseElement.getTextOffset();
    }

    private final class RestServiceItemPresentation implements ItemPresentation {
        @Override
        @Nullable
        public String getPresentableText() {
            return url;
        }

        @Override
        @Nullable
        public String getLocationString() {
            if (psiElement instanceof PsiMethod psiMethod) {
                PsiClass containingClass = psiMethod.getContainingClass();
                if (containingClass != null) {
                    String modulePart = module == null ? "" : module.getName() + " ";
                    return "(" + modulePart + containingClass.getName() + "#" + psiMethod.getName() + ")";
                }
            }

            return psiElement.getContainingFile() == null
                ? null
                : "(" + psiElement.getContainingFile().getName() + ")";
        }

        @Override
        @Nullable
        public Icon getIcon(boolean unused) {
            return ToolkitIcons.METHOD.get(method);
        }
    }
}
