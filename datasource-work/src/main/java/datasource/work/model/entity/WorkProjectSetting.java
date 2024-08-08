package datasource.work.model.entity;

import datasource.work.model.converter.ListToStringConverter;
import datasource.work.model.converter.MapToStringConverter;
import datasource.work.model.enumeration.BuildType;
import datasource.work.model.enumeration.JavaVersion;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "PROJECT_SET")
public class WorkProjectSetting {
    @Id
    @Column(name = "PROJECT_ID")
    private String id;

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
    private String workDirectory; //WORK_DIR

    @Setter
    @Column(name = "ENV_VARIABLE")
    @Convert(converter = MapToStringConverter.class)
    private Map<String, String> envVariables;

    @Setter
    @Column(name = "EXECUTE_PARAMETER")
    @Convert(converter = ListToStringConverter.class)
    private List<String> executeParameters;

    public WorkProjectSetting(String id, BuildType buildType, JavaVersion javaVersion, int port, String workDirectory, Map<String, String> envVariables, List<String> executeParameters) {
        this.id = id;
        this.buildType = buildType;
        this.javaVersion = javaVersion;
        this.port = port;
        this.workDirectory = workDirectory;
        this.envVariables = envVariables;
        this.executeParameters = executeParameters;
    }

    public WorkProjectSetting(BuildType buildType, JavaVersion javaVersion, int port, String workDirectory, Map<String, String> envVariables, List<String> executeParameters) {
        this.buildType = buildType;
        this.javaVersion = javaVersion;
        this.port = port;
        this.workDirectory = workDirectory;
        this.envVariables = envVariables;
        this.executeParameters = executeParameters;
    }
}
