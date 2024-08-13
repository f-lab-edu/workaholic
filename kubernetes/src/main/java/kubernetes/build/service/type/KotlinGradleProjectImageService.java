package kubernetes.build.service.type;

import datasource.work.model.entity.WorkProjectSetting;
import kubernetes.exception.type.FailedInjectionJibDependency;
import kubernetes.exception.type.FailedReadBuildFileException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class KotlinGradleProjectImageService {
    private boolean checkExistsJibDependency(List<String> buildFileLines) {
        return buildFileLines.stream().anyMatch(
                line -> line.contains("id(\"com.google.cloud.tools.jib\")"));
    }

    private int findPluginsBlockStartIndex(List<String> buildFileLines) {
        for (int index = 0; index < buildFileLines.size(); index++) {
            if (buildFileLines.get(index).trim().equals("plugins {")) {
                return index;
            }
        }
        return -1;
    }

    private int findPluginsBlockEndIndex(List<String> buildFileLines, int startIndex) {
        for (int i = startIndex + 1; i < buildFileLines.size(); i++) {
            if (buildFileLines.get(i).trim().equals("}")) {
                return i;
            }
        }
        return -1;
    }

    private void injectionJibDependency(Path buildFilePath, List<String> buildFileLines) {
        if(!checkExistsJibDependency(buildFileLines)) {
            int pluginsBlockStartIndex = findPluginsBlockStartIndex(buildFileLines);
            if (pluginsBlockStartIndex != -1) {
                int pluginBlockEndIndex = findPluginsBlockEndIndex(buildFileLines, pluginsBlockStartIndex);
                buildFileLines.add(pluginBlockEndIndex, "  id(\"com.google.cloud.tools.jib\") version \"3.2.0\"");
            } else {
                buildFileLines.add(0, "plugins {");
                buildFileLines.add(1, "  id(\"com.google.cloud.tools.jib\") version \"3.2.0\"");
                buildFileLines.add(2, "}");
            }

            try {
                Files.write(buildFilePath, buildFileLines);
            } catch (IOException e) {
                throw new FailedInjectionJibDependency();
            }
        }
    }

    private void injectionJibConfiguration(Path buildFilePath, List<String> buildFileLines) {
        int jibConfigIndex = buildFileLines.indexOf("jib {");
        if (jibConfigIndex == -1) {
            buildFileLines.add("");
            buildFileLines.add("jib {");
            buildFileLines.add("    from {");
            buildFileLines.add("        image = \"" + "openjdk:11-jdk" + "\"");
            buildFileLines.add("    }");
            buildFileLines.add("    to {");
            buildFileLines.add("        image = \"parklibra1011/test\"");
            buildFileLines.add("        tags = [\"latest\"]");
            buildFileLines.add("    }");
            buildFileLines.add("    container {");
            buildFileLines.add("        jvmFlags = [\"-Xms128m\", \"-Xmx128m\"]");
            buildFileLines.add("    }");
            buildFileLines.add("}");
        }

        try {
            Files.write(buildFilePath, buildFileLines);
        } catch (IOException e) {
            throw new FailedInjectionJibDependency();
        }
    }

    public void jibSetting(String projectPath, WorkProjectSetting projectSetting) {
        Path buildFilePath = Paths.get(projectPath, "build.gradle.kts");
        List<String> buildFileLines;

        try {
            buildFileLines = new ArrayList<>(Files.readAllLines(buildFilePath));
        } catch (IOException e) {
            throw new FailedReadBuildFileException();
        }

        injectionJibDependency(buildFilePath, buildFileLines);
        injectionJibConfiguration(buildFilePath, buildFileLines);
    }
}
