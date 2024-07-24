package com.project.workaholic.project.model.enumeration;

import lombok.Getter;

@Getter
public enum BuildType {
    MAVEN("./mvnw clean package", "target/*.jar"),
    GRADLE("gradle clean bootJar", "build/libs/*.jar");

    private final String buildCommand;
    private final String jarFilePath;

    BuildType(String buildCommand, String jarFilePath) {
        this.buildCommand = buildCommand;
        this.jarFilePath = jarFilePath;
    }
}
