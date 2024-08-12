package workaholic.model;

import datasource.work.model.enumeration.BuildType;
import datasource.work.model.enumeration.JavaVersion;
import datasource.work.model.enumeration.VCSVendor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class WorkaholicResponseDTO {
    private final String id;

    private final VCSVendor vendor;

    private final JavaVersion javaVersion;

    private final BuildType buildType;

    private final String workDirectory;

    private final int port;

    private final Map<String, String> envVariables;

    private final List<String> args;

    public WorkaholicResponseDTO(String id, VCSVendor vendor, JavaVersion javaVersion, BuildType buildType, String workDirectory, int port, Map<String, String> envVariables, List<String> args) {
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
