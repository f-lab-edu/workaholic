package com.project.work.model.entity;

import com.project.work.model.converter.ListToStringConverter;
import com.project.work.model.converter.MapToStringConverter;
import com.project.work.model.enumeration.BuildType;
import com.project.work.model.enumeration.JavaVersion;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Setter
    @Column(name = "BUILD_TYPE")
    @Enumerated(EnumType.STRING)
    private BuildType buildType;

    @Setter
    @Column(name = "JAVA_VERSION")
    @Enumerated(EnumType.STRING)
    private JavaVersion javaVersion;//FROM

    @Setter
    @Column(name = "PORT")
    private int port;

    @Setter
    @Column(name = "WORK_DIR")
    private String workDir; //WORK_DIR

    @Setter
    @Column(name = "ENV_VARIABLE")
    @Convert(converter = MapToStringConverter.class)
    private Map<String, String> envVariables;

    @Setter
    @Column(name = "EXECUTE_PARAMETER")
    @Convert(converter = ListToStringConverter.class)
    private List<String> executeParameters;

    public WorkProjectSetting(UUID id) {
        this.id = id;
    }
}
