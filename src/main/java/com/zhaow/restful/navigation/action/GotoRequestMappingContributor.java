package com.zhaow.restful.navigation.action;

import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.zhaow.restful.common.ServiceHelper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class GotoRequestMappingContributor implements ChooseByNameContributor {
    private final Module myModule;
    private volatile List<RestServiceItem> navItems = List.of();

    public GotoRequestMappingContributor(Module myModule) {
        this.myModule = myModule;
    }

    @Override
    @NotNull
    public String[] getNames(Project project, boolean onlyThisModuleChecked) {
        List<RestServiceItem> itemList;
        if (onlyThisModuleChecked && myModule != null) {
            itemList = ServiceHelper.buildRestServiceItemListUsingResolver(myModule);
        } else {
            itemList = ServiceHelper.buildRestServiceItemListUsingResolver(project);
        }

        navItems = new ArrayList<>(itemList);
        return itemList.stream().map(RestServiceItem::getName).toArray(String[]::new);
    }

    @Override
    @NotNull
    public NavigationItem[] getItemsByName(String name, String pattern, Project project, boolean onlyThisModuleChecked) {
        return navItems.stream()
            .filter(item -> name.equals(item.getName()))
            .toArray(NavigationItem[]::new);
    }
}
