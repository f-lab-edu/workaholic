package com.project.workaholic.project.model.enumeration;

import lombok.Getter;

@Getter
public enum BaseJavaVersion {
    JAVA_8("8-jdk"),
    JAVA_11("11-jdk"),
    JAVA_17("17-jdk"),
    JAVA_21("21-jdk");

    private final String imageName;

    BaseJavaVersion(String imageName) {
        this.imageName = imageName;
    }
}
