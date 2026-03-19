package com.zhaow.restful.common;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.zhaow.restful.common.resolver.JaxrsResolver;
import com.zhaow.restful.common.resolver.SpringResolver;
import com.zhaow.restful.navigation.action.RestServiceItem;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ServiceHelper {
    private ServiceHelper() {
    }

    @NotNull
    public static List<RestServiceItem> buildRestServiceItemListUsingResolver(@NotNull Module module) {
        return distinctItems(List.of(
            new SpringResolver(module).findAllSupportedServiceItemsInModule(),
            new JaxrsResolver(module).findAllSupportedServiceItemsInModule()
        ));
    }

    @NotNull
    public static List<RestServiceItem> buildRestServiceItemListUsingResolver(@NotNull Project project) {
        return distinctItems(List.of(
            new SpringResolver(project).findAllSupportedServiceItemsInProject(),
            new JaxrsResolver(project).findAllSupportedServiceItemsInProject()
        ));
    }

    @NotNull
    private static List<RestServiceItem> distinctItems(@NotNull List<List<RestServiceItem>> batches) {
        Map<String, RestServiceItem> uniqueItems = new LinkedHashMap<>();
        for (List<RestServiceItem> batch : batches) {
            for (RestServiceItem item : batch) {
                uniqueItems.putIfAbsent(item.getDedupKey(), item);
            }
        }
        return new ArrayList<>(uniqueItems.values());
    }
}
