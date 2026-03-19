package com.zhaow.restful.common.resolver;

import com.zhaow.restful.navigation.action.RestServiceItem;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ServiceResolver {
    @NotNull
    List<RestServiceItem> findAllSupportedServiceItemsInModule();

    @NotNull
    List<RestServiceItem> findAllSupportedServiceItemsInProject();
}
