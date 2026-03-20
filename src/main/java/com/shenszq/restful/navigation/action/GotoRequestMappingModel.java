package com.shenszq.restful.navigation.action;

import com.intellij.ide.IdeBundle;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.ide.util.gotoByName.CustomMatcherModel;
import com.intellij.ide.util.gotoByName.FilteringGotoByModel;
import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.shenszq.restful.common.UrlPatternUtils;
import com.shenszq.restful.method.HttpMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.ListCellRenderer;
import java.util.Collection;

public final class GotoRequestMappingModel extends FilteringGotoByModel<HttpMethod> implements DumbAware, CustomMatcherModel {
    private static final String ONLY_CURRENT_MODULE_KEY = "GoToRestService.OnlyCurrentModule";

    public GotoRequestMappingModel(@NotNull Project project, @NotNull ChooseByNameContributor[] contributors) {
        super(project, contributors);
    }

    @Override
    @Nullable
    protected HttpMethod filterValueFor(NavigationItem item) {
        return item instanceof RestServiceItem serviceItem ? serviceItem.getMethod() : null;
    }

    @Override
    public String getPromptText() {
        return "Enter service URL path:";
    }

    @Override
    public String getNotInMessage() {
        return IdeBundle.message("label.no.matches.found.in.project");
    }

    @Override
    public String getNotFoundMessage() {
        return IdeBundle.message("label.no.matches.found");
    }

    @Override
    public char getCheckBoxMnemonic() {
        return 'N';
    }

    @Override
    public boolean loadInitialCheckBoxState() {
        return PropertiesComponent.getInstance(myProject).getBoolean(ONLY_CURRENT_MODULE_KEY, false);
    }

    @Override
    public void saveInitialCheckBoxState(boolean state) {
        PropertiesComponent.getInstance(myProject).setValue(ONLY_CURRENT_MODULE_KEY, state, false);
    }

    @Override
    @Nullable
    public String getFullName(Object element) {
        return getElementName(element);
    }

    @Override
    @NotNull
    public String[] getSeparators() {
        return new String[]{"/", "?"};
    }

    @Override
    @Nullable
    public String getCheckBoxName() {
        return "当前模块";
    }

    @Override
    public boolean willOpenEditor() {
        return true;
    }

    @Override
    public boolean matches(@NotNull String popupItem, @NotNull String userPattern) {
        return UrlPatternUtils.matches(popupItem, userPattern);
    }

    @Override
    @NotNull
    public String removeModelSpecificMarkup(@NotNull String pattern) {
        return UrlPatternUtils.normalizeUserInput(pattern);
    }

    @Override
    @Nullable
    protected Collection<HttpMethod> getFilterItems() {
        return super.getFilterItems();
    }

    @Override
    public ListCellRenderer<?> getListCellRenderer() {
        return super.getListCellRenderer();
    }
}
