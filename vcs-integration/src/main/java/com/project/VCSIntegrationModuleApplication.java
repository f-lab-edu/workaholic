package com.project;

import com.project.config.VCSProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(VCSProperties.class)
public class VCSIntegrationModuleApplication {
    public static void main(String[] args) {
        SpringApplication.run(VCSIntegrationModuleApplication.class, args);
    }
}