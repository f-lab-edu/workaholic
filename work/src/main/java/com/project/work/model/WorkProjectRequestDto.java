package com.project.work.model;

import com.project.work.model.enumeration.BuildType;
import com.project.work.model.enumeration.JavaVersion;
import com.project.work.model.enumeration.VCSVendor;
import com.project.work.model.validation.ValidEnumeration;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkProjectRequestDto {
    private String id;

    @ValidEnumeration(enumClass = VCSVendor.class)
    private VCSVendor vendor = VCSVendor.GITHUB;

    @ValidEnumeration(enumClass = JavaVersion.class)
    private JavaVersion javaVersion = JavaVersion.JAVA_21;

    @ValidEnumeration(enumClass = BuildType.class)
    private BuildType buildType = BuildType.GRADLE;

    private String workDirectory = "";

    private int port = 8080;

    private Map<String, String> envVariables = new HashMap<>();

    private List<String> args;
}
