package com.shenszq.restful.method;

public final class RequestPath {
    private String path;
    private final String method;

    public RequestPath(String path, String method) {
        this.path = path;
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public String getMethod() {
        return method;
    }

    public void concat(RequestPath classRequestPath) {
        String classUri = classRequestPath.getPath();
        String methodUri = path;
        if (!classUri.startsWith("/")) {
            classUri = "/" + classUri;
        }
        if (!classUri.endsWith("/")) {
            classUri = classUri + "/";
        }
        if (methodUri.startsWith("/")) {
            methodUri = methodUri.substring(1);
        }
        path = classUri + methodUri;
    }
}
