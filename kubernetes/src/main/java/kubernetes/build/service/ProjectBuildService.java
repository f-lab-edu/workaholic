package kubernetes.build.service;

import datasource.work.model.entity.WorkProjectSetting;
import kubernetes.build.service.type.GradleProjectImageService;
import kubernetes.build.service.type.KotlinGradleProjectImageService;
import kubernetes.build.service.type.MavenProjectImageService;
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
        switch (projectSetting.getBuildType()) {
            case MAVEN -> mavenProjectImageService.jibSetting(projectPath, projectSetting);
            case GRADLE -> gradleProjectImageService.build(projectPath, projectSetting);
            case KOTLIN -> kotlinGradleProjectImageService.jibSetting(projectPath, projectSetting);
        }
    }
}
