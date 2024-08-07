package com.project.kubernetes.build.model;

import com.project.kubernetes.build.model.enumeration.BuildType;
import lombok.Getter;
import org.springframework.boot.system.JavaVersion;

import java.util.List;
import java.util.Map;

@Getter
public class ProjectBuild {
    private final String id;
    private final JavaVersion javaVersion;
    private final BuildType buildType;
    private final String workDirectory;
    private final Map<String, String> envVariables;

    public ProjectBuild(String id, JavaVersion javaVersion, BuildType buildType, String workDirectory, Map<String, String> envVariables, List<String> args) {
        this.id = id;
        this.javaVersion = javaVersion;
        this.buildType = buildType;
        this.workDirectory = workDirectory;
        this.envVariables = envVariables;
    }
}
