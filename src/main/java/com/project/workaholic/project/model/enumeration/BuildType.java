package com.project.workaholic.project.model.enumeration;

import lombok.Getter;

import java.util.List;

@Getter
public enum BuildType {
    MAVEN(List.of("./mvnw", "clean", "package"), "target/*.jar"),
    GRADLE(List.of("./gradlew", "clean", "build"), "build/libs/*.jar");

    private final List<String> commands;
    private final String jarFilePath;

    BuildType(List<String> commands, String jarFilePath) {
        this.commands = commands;
        this.jarFilePath = jarFilePath;
    }
}
