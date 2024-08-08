package kubernetes.build.service;

import kubernetes.build.model.ProjectBuild;
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

    public void buildImage(ProjectBuild dto) {
        switch (dto.getBuildType()) {

        }
    }
}
