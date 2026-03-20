package com.shenszq.restful.navigation.action;

import com.intellij.ide.util.gotoByName.ChooseByNameModel;
import com.intellij.ide.util.gotoByName.ChooseByNameViewModel;
import com.intellij.ide.util.gotoByName.DefaultChooseByNameItemProvider;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.psi.PsiElement;
import com.intellij.util.Processor;
import com.shenszq.restful.common.UrlPatternUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashSet;
import java.util.Set;

public final class GotoRequestMappingProvider extends DefaultChooseByNameItemProvider {
    public GotoRequestMappingProvider(@Nullable PsiElement context) {
        super(context);
    }

    @Override
    public boolean filterElements(@NotNull ChooseByNameViewModel base,
                                  @NotNull String pattern,
                                  boolean everywhere,
                                  @NotNull ProgressIndicator indicator,
                                  @NotNull Processor<Object> consumer) {
        String normalizedPattern = UrlPatternUtils.normalizeUserInput(pattern);
        ChooseByNameModel model = base.getModel();
        Set<Object> processed = new LinkedHashSet<>();

        for (String name : model.getNames(everywhere)) {
            indicator.checkCanceled();
            if (!UrlPatternUtils.matches(name, normalizedPattern)) {
                continue;
            }

            for (Object element : model.getElementsByName(name, everywhere, normalizedPattern)) {
                indicator.checkCanceled();
                if (!processed.add(element)) {
                    continue;
                }
                if (!consumer.process(element)) {
                    return false;
                }
            }
        }

        return true;
    }
}
