package com.shenszq.restful.navigation.action;

import com.intellij.ide.actions.GotoActionBase;
import com.intellij.ide.util.gotoByName.ChooseByNameFilter;
import com.intellij.ide.util.gotoByName.ChooseByNameItemProvider;
import com.intellij.ide.util.gotoByName.ChooseByNameModel;
import com.intellij.ide.util.gotoByName.ChooseByNamePopup;
import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.shenszq.restful.method.HttpMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;
import java.awt.datatransfer.DataFlavor;
import java.util.Arrays;
import java.util.List;

public final class GotoRequestMappingAction extends GotoActionBase implements DumbAware {
    @Override
    protected void gotoActionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) {
            return;
        }

        ChooseByNameContributor[] contributors = {
            new GotoRequestMappingContributor(LangDataKeys.MODULE.getData(e.getDataContext()))
        };
        GotoRequestMappingModel model = new GotoRequestMappingModel(project, contributors);

        GotoActionCallback<HttpMethod> callback = new GotoActionCallback<>() {
            @Override
            protected ChooseByNameFilter<HttpMethod> createFilter(@NotNull ChooseByNamePopup popup) {
                return new GotoRequestMappingFilter(popup, model, project);
            }

            @Override
            public void elementChosen(ChooseByNamePopup chooseByNamePopup, Object element) {
                if (element instanceof RestServiceItem navigationItem && navigationItem.canNavigate()) {
                    navigationItem.navigate(true);
                }
            }
        };

        GotoRequestMappingProvider provider = new GotoRequestMappingProvider(getPsiContext(e));
        showNavigationPopup(
            e,
            model,
            callback,
            "Request Mapping URL matching pattern",
            true,
            true,
            provider
        );
    }

    protected <T> void showNavigationPopup(AnActionEvent e,
                                           ChooseByNameModel model,
                                           GotoActionCallback<T> callback,
                                           @Nullable String findUsagesTitle,
                                           boolean useSelectionFromEditor,
                                           boolean allowMultipleSelection,
                                           ChooseByNameItemProvider itemProvider) {
        Project project = e.getData(CommonDataKeys.PROJECT);
        Pair<String, Integer> start = getInitialText(useSelectionFromEditor, e);
        String predefinedText = start.getFirst() == null ? tryFindCopiedUrl() : start.getFirst();

        showNavigationPopup(
            callback,
            findUsagesTitle,
            RestServiceChooseByNamePopup.createPopup(
                project,
                model,
                itemProvider,
                predefinedText,
                false,
                start.getSecond()
            ),
            allowMultipleSelection
        );
    }

    @Nullable
    private String tryFindCopiedUrl() {
        String content = CopyPasteManager.getInstance().getContents(DataFlavor.stringFlavor);
        if (content == null) {
            return null;
        }

        String trimmed = content.trim();
        if (trimmed.startsWith("http://") || trimmed.startsWith("https://") || trimmed.startsWith("/")) {
            return trimmed.length() <= 240 ? trimmed : trimmed.substring(0, 240);
        }
        return null;
    }

    private static final class GotoRequestMappingFilter extends ChooseByNameFilter<HttpMethod> {
        private GotoRequestMappingFilter(@NotNull ChooseByNamePopup popup,
                                         @NotNull GotoRequestMappingModel model,
                                         @NotNull Project project) {
            super(popup, model, GotoRequestMappingConfiguration.getInstance(project), project);
        }

        @Override
        @NotNull
        protected List<HttpMethod> getAllFilterValues() {
            return Arrays.asList(HttpMethod.values());
        }

        @Override
        protected String textForFilterValue(@NotNull HttpMethod value) {
            return value.name();
        }

        @Override
        protected Icon iconForFilterValue(@NotNull HttpMethod value) {
            return null;
        }
    }
}
