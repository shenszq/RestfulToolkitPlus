package com.zhaow.restful.annotations;

public enum JaxrsPathAnnotation implements PathMappingAnnotation {
    JAVAX_PATH("Path", "javax.ws.rs.Path"),
    JAKARTA_PATH("Path", "jakarta.ws.rs.Path");

    private final String shortName;
    private final String qualifiedName;

    JaxrsPathAnnotation(String shortName, String qualifiedName) {
        this.shortName = shortName;
        this.qualifiedName = qualifiedName;
    }

    @Override
    public String getQualifiedName() {
        return qualifiedName;
    }

    @Override
    public String getShortName() {
        return shortName;
    }
}
