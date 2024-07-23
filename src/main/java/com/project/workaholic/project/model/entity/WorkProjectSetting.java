package com.project.workaholic.project.model.entity;

import com.project.workaholic.config.converter.ListToStringConverter;
import com.project.workaholic.config.converter.MapToStringConverter;
import com.project.workaholic.project.model.enumeration.BaseJavaVersion;
import com.project.workaholic.project.model.enumeration.BuildType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "PROJECT_SET")
public class WorkProjectSetting {
    @Id
    @Column(name = "PROJECT_ID")
    private UUID id;

    @Column(name = "BUILD_TYPE")
    @Enumerated(EnumType.STRING)
    private BuildType buildType;

    @Column(name = "JAVA_VERSION")
    @Enumerated(EnumType.STRING)
    private BaseJavaVersion baseJavaVersion;//FROM

    @Column(name = "PORT")
    private int port;

    @Column(name = "WORK_DIR")
    private String workDir; //WORK_DIR

    @Column(name = "ENV_VARIABLE")
    @Convert(converter = MapToStringConverter.class)
    private Map<String, String> envVariables;

    @Column(name = "EXECUTE_PARAMETER")
    @Convert(converter = ListToStringConverter.class)
    private List<String> executeParameters;

    public WorkProjectSetting(UUID id, BuildType buildType, BaseJavaVersion baseJavaVersion, int port, String workDir, Map<String, String> envVariables, List<String> executeParameters) {
        this.id = id;
        this.buildType = buildType;
        this.baseJavaVersion = baseJavaVersion;
        this.port = port;
        this.workDir = workDir;
        this.envVariables = envVariables;
        this.executeParameters = executeParameters;
    }
}
