package com.project.work.model;

import datasource.work.model.enumeration.BuildType;
import datasource.work.model.enumeration.JavaVersion;
import com.project.work.model.validation.ValidEnumeration;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkProjectUpdateDto {
    @ValidEnumeration(enumClass = JavaVersion.class)
    private JavaVersion javaVersion;

    @ValidEnumeration(enumClass = BuildType.class)
    private BuildType buildType;

    private String workDirectory;

    private int port;

    private Map<String, String> envVariables;

    private List<String> args;
}

