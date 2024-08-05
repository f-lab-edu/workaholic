package com.project;

import com.project.github.service.GithubProperties;
import com.project.gitlab.service.GitlabProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({GithubProperties.class, GitlabProperties.class})
public class VCSIntegrationModuleApplication {
    public static void main(String[] args) {
        SpringApplication.run(VCSIntegrationModuleApplication.class, args);
    }
}