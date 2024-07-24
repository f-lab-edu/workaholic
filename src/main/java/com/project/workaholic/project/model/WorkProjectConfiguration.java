package com.project.workaholic.project.model;

import com.project.workaholic.config.validation.ValidEnum;
import com.project.workaholic.project.model.enumeration.BaseJavaVersion;
import com.project.workaholic.project.model.enumeration.BuildType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkProjectConfiguration {
    @Schema(description = "Container 에서 실행 시킬 JDK Version")
    @ValidEnum(enumClass = BaseJavaVersion.class)
    private BaseJavaVersion jdkVersion = BaseJavaVersion.JAVA_8;

    @Schema(description = "컨테이너 연결 Port")
    private int port;

    @Schema(description = "Container 에서 실행 시킬 JDK Version")
    @ValidEnum(enumClass = BuildType.class)
    private BuildType buildTool = BuildType.MAVEN;

    @Schema(description = "프로젝트 루트 디렉토리")
    private String rootDirectory;

    @Schema(description = "환경 변수")
    private Map<String, String> envVariables = new HashMap<>();

    @Schema(description = "환경 변수")
    private List<String> executeParameters = new ArrayList<>();

    public WorkProjectConfiguration(BaseJavaVersion jdkVersion, int port, BuildType buildTool, String rootDirectory, Map<String, String> envVariables, List<String> executeParameters) {
        this.jdkVersion = jdkVersion;
        this.port = port;
        this.buildTool = buildTool;
        this.rootDirectory = rootDirectory;
        this.envVariables = envVariables;
        this.executeParameters = executeParameters;
    }
}
