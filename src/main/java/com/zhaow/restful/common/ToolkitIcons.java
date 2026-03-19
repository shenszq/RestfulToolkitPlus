package com.zhaow.restful.common;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.IconLoader;
import com.zhaow.restful.method.HttpMethod;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;

public final class ToolkitIcons {
    private ToolkitIcons() {
    }

    public static final Icon SERVICE = IconLoader.getIcon("/icons/service.png", ToolkitIcons.class);

    public static final class METHOD {
        public static final Icon GET = IconLoader.getIcon("/icons/method/g.png", ToolkitIcons.class);
        public static final Icon POST = IconLoader.getIcon("/icons/method/p.png", ToolkitIcons.class);
        public static final Icon PUT = IconLoader.getIcon("/icons/method/p2.png", ToolkitIcons.class);
        public static final Icon PATCH = IconLoader.getIcon("/icons/method/p3.png", ToolkitIcons.class);
        public static final Icon DELETE = IconLoader.getIcon("/icons/method/d.png", ToolkitIcons.class);
        public static final Icon UNDEFINED = IconLoader.getIcon("/icons/method/undefined.png", ToolkitIcons.class);

        private METHOD() {
        }

        public static Icon get(@Nullable HttpMethod method) {
            if (method == null) {
                return UNDEFINED;
            }
            return switch (method) {
                case GET -> GET;
                case POST -> POST;
                case PUT -> PUT;
                case PATCH -> PATCH;
                case DELETE -> DELETE;
                default -> AllIcons.Actions.Search;
            };
        }
    }
}
