package com.zhaow.restful.navigation.action;

import com.intellij.ide.util.gotoByName.ChooseByNameFilterConfiguration;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import com.intellij.openapi.project.Project;
import com.zhaow.restful.method.HttpMethod;
import org.jetbrains.annotations.NotNull;

@State(name = "GotoRequestMappingConfiguration", storages = @Storage(StoragePathMacros.WORKSPACE_FILE))
public final class GotoRequestMappingConfiguration extends ChooseByNameFilterConfiguration<HttpMethod> {
    public static GotoRequestMappingConfiguration getInstance(@NotNull Project project) {
        return project.getService(GotoRequestMappingConfiguration.class);
    }

    @Override
    protected String nameForElement(@NotNull HttpMethod type) {
        return type.name();
    }
}
