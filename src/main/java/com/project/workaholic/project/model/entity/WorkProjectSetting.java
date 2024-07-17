package com.project.workaholic.project.model.entity;

import com.project.workaholic.config.converter.MapToToStringConverter;
import com.project.workaholic.project.model.enumeration.BaseJavaVersion;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "PROJECT_SET")
public class WorkProjectSetting {
    @Id
    @Column(name = "PROJECT_ID")
    private String id;

    @Column(name = "JAVA_VERSION")
    @Enumerated(EnumType.STRING)
    private BaseJavaVersion baseJavaVersion;//FROM

    @Column(name = "WORK_DIR")
    private String workDir; //WORK_DIR

    @Column(name = "ENV_VARIABLE")
    @Convert(converter = MapToToStringConverter.class)
    private Map<String, String> envVariables;//ENTRYPOINT

    public WorkProjectSetting(String id, BaseJavaVersion baseJavaVersion, String workDir, Map<String, String> envVariables) {
        this.id = id;
        this.baseJavaVersion = baseJavaVersion;
        this.workDir = workDir;
        this.envVariables = envVariables;
    }
}
