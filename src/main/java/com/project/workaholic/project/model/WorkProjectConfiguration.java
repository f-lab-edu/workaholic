package com.project.workaholic.project.model;

import com.project.workaholic.config.validation.ValidEnum;
import com.project.workaholic.project.model.enumeration.BaseJavaVersion;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkProjectConfiguration {
    @Schema(description = "Container 에서 실행 시킬 JDK Version")
    @ValidEnum(enumClass = BaseJavaVersion.class)
    private BaseJavaVersion jdkVersion = BaseJavaVersion.JAVA_8;

    @Schema(description = "컨테이너 위치 지정")
    private String rootDirectory = "";

    @Schema(description = "환경 변수")
    private Map<String, String> variables = new HashMap<>();

    public WorkProjectConfiguration(BaseJavaVersion jdkVersion, String rootDirectory, Map<String, String> variables) {
        this.jdkVersion = jdkVersion;
        this.rootDirectory = rootDirectory;
        this.variables = variables;
    }
}
