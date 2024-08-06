package com.project.work.model;

import common.model.enumeration.BuildType;
import common.model.enumeration.JavaVersion;
import common.model.enumeration.VCSVendor;
import common.model.validation.ValidEnumeration;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class WorkProjectResponseDto {
    private final String id;

    @ValidEnumeration(enumClass = VCSVendor.class)
    private final VCSVendor vendor;

    @ValidEnumeration(enumClass = JavaVersion.class)
    private final JavaVersion javaVersion;

    @ValidEnumeration(enumClass = BuildType.class)
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
