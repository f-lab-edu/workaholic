package workaholic.model;

import datasource.work.model.enumeration.BuildType;
import datasource.work.model.enumeration.JavaVersion;
import workaholic.model.validation.ValidEnumeration;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkaholicUpdateDTO {
    @ValidEnumeration(enumClass = JavaVersion.class)
    private JavaVersion javaVersion = JavaVersion.JAVA_17;

    @ValidEnumeration(enumClass = BuildType.class)
    private BuildType buildType = BuildType.GRADLE;

    private String workDirectory;

    private int targetPort = 8080;

    private int nodePort = 30080;

    private Map<String, String> envVariables = new HashMap<>();

    private List<String> args = new ArrayList<>();
}

