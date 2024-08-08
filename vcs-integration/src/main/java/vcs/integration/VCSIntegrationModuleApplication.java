package vcs.integration;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@ConfigurationPropertiesScan({"rabbit.message.queue", "vcs.integration"})
@SpringBootApplication(scanBasePackages = {"vcs.integration", "com.project.datasource.work", "rabbit.message.queue"})
public class VCSIntegrationModuleApplication {
    public static void main(String[] args) {
        SpringApplication.run(VCSIntegrationModuleApplication.class, args);
    }
}