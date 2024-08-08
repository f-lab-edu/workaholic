package kubernetes.build.service;

import datasource.work.model.entity.WorkProjectSetting;
import org.springframework.stereotype.Service;

@Service
public class ProjectBuildService {
    private final GradleProjectImageService gradleProjectImageService;
    private final KotlinGradleProjectImageService kotlinGradleProjectImageService;
    private final MavenProjectImageService mavenProjectImageService;

    public ProjectBuildService(GradleProjectImageService gradleProjectImageService, KotlinGradleProjectImageService kotlinGradleProjectImageService, MavenProjectImageService mavenProjectImageService) {
        this.gradleProjectImageService = gradleProjectImageService;
        this.kotlinGradleProjectImageService = kotlinGradleProjectImageService;
        this.mavenProjectImageService = mavenProjectImageService;
    }

    public void buildImage(String projectPath, WorkProjectSetting projectSetting) {
        kotlinGradleProjectImageService.jibSetting(projectPath, projectSetting.getJavaVersion());
    }
}
