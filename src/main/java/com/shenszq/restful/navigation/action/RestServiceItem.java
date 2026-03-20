package com.shenszq.restful.navigation.action;

import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.module.Module;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.shenszq.restful.common.ToolkitIcons;
import com.shenszq.restful.method.HttpMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;

public final class RestServiceItem implements NavigationItem {
    private final PsiElement psiElement;
    private final String url;
    private final HttpMethod method;
    private final String locationString;
    private final String dedupKey;

    public RestServiceItem(@NotNull PsiElement psiElement,
                           @Nullable String requestMethod,
                           @NotNull String url,
                           @Nullable Module module) {
        this.psiElement = psiElement;
        this.url = url;
        this.method = HttpMethod.getByRequestMethod(requestMethod);
        String moduleName = module == null ? null : module.getName();
        this.locationString = ReadAction.compute(() -> buildLocationString(psiElement, moduleName));
        this.dedupKey = ReadAction.compute(() -> buildDedupKey(psiElement, url, method));
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
        Navigatable navigatable = ReadAction.compute(() -> {
            PsiElement navigationElement = psiElement.getNavigationElement();
            return navigationElement instanceof Navigatable candidate ? candidate : null;
        });
        if (navigatable != null && navigatable.canNavigate()) {
            navigatable.navigate(requestFocus);
        }
    }

    @Override
    public boolean canNavigate() {
        Navigatable navigatable = ReadAction.compute(() -> {
            PsiElement navigationElement = psiElement.getNavigationElement();
            return navigationElement instanceof Navigatable candidate ? candidate : null;
        });
        return navigatable != null && navigatable.canNavigate();
    }

    @Override
    public boolean canNavigateToSource() {
        return canNavigate();
    }

    @Nullable
    public HttpMethod getMethod() {
        return method;
    }

    @NotNull
    public String getDedupKey() {
        return dedupKey;
    }

    @NotNull
    private static String buildDedupKey(@NotNull PsiElement psiElement, @NotNull String url, @Nullable HttpMethod method) {
        PsiElement navigationElement = psiElement.getNavigationElement();
        PsiElement baseElement = navigationElement != null ? navigationElement : psiElement;
        PsiFile containingFile = baseElement.getContainingFile();
        String fileKey;
        if (containingFile != null && containingFile.getVirtualFile() != null) {
            fileKey = containingFile.getVirtualFile().getPath();
        } else if (containingFile != null) {
            fileKey = containingFile.getName();
        } else {
            fileKey = baseElement.getClass().getName();
        }
        return url + "|" + method + "|" + fileKey + "|" + baseElement.getTextOffset();
    }

    @Nullable
    private static String buildLocationString(@NotNull PsiElement psiElement, @Nullable String moduleName) {
        if (psiElement instanceof PsiMethod psiMethod) {
            PsiClass containingClass = psiMethod.getContainingClass();
            if (containingClass != null) {
                String modulePart = moduleName == null ? "" : moduleName + " ";
                return "(" + modulePart + containingClass.getName() + "#" + psiMethod.getName() + ")";
            }
        }

        PsiFile containingFile = psiElement.getContainingFile();
        return containingFile == null ? null : "(" + containingFile.getName() + ")";
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
            return locationString;
        }

        @Override
        @Nullable
        public Icon getIcon(boolean unused) {
            return ToolkitIcons.METHOD.get(method);
        }
    }
}
