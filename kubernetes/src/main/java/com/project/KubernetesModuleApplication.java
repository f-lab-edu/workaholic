package com.project;

import com.project.kubernetes.config.KubernetesConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(KubernetesConfig.class)
public class KubernetesModuleApplication {
    public static void main(String[] args) {
        SpringApplication.run(KubernetesModuleApplication.class, args);
    }
}