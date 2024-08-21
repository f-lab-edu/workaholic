package workaholic.model;

import datasource.work.model.enumeration.BuildType;
import datasource.work.model.enumeration.JavaVersion;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
public class WorkaholicResponseDTO {
    private final String id;

    private final JavaVersion javaVersion;

    private final BuildType buildType;

    private final String workDirectory;

    private final int targetPort;

    private final int nodePort;

    private final Map<String, String> envVariables;

    private final List<String> args;

    public WorkaholicResponseDTO(String id, JavaVersion javaVersion, BuildType buildType, String workDirectory, int targetPort, int nodePort, Map<String, String> envVariables, List<String> args) {
        this.id = id;
        this.javaVersion = javaVersion;
        this.buildType = buildType;
        this.workDirectory = workDirectory;
        this.targetPort = targetPort;
        this.nodePort = nodePort;
        this.envVariables = envVariables;
        this.args = args;
    }
}
