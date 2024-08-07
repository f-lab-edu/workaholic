package com.project.work.model;

import com.project.work.model.enumeration.BuildType;
import com.project.work.model.enumeration.JavaVersion;
import com.project.work.model.enumeration.VCSVendor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class WorkProjectResponseDto {
    private final String id;

    private final VCSVendor vendor;

    private final JavaVersion javaVersion;

    private final BuildType buildType;

    private final String workDirectory;

    private final int port;

    private final Map<String, String> envVariables;

    private final List<String> args;

    public WorkProjectResponseDto(String id, VCSVendor vendor, JavaVersion javaVersion, BuildType buildType, String workDirectory, int port, Map<String, String> envVariables, List<String> args) {
        this.id = id;
        this.vendor = vendor;
        this.javaVersion = javaVersion;
        this.buildType = buildType;
        this.workDirectory = workDirectory;
        this.port = port;
        this.envVariables = envVariables;
        this.args = args;
    }
}
