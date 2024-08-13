package vcs.integration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ConfigurationPropertiesScan({"rabbit.message.queue", "vcs.integration"})
@EntityScan(basePackages = "datasource.work.model.entity")
@EnableJpaRepositories(basePackages = "datasource.work.repository")
@SpringBootApplication(scanBasePackages = {"datasource.work", "rabbit.message.queue", "vcs.integration"})
public class VCSIntegrationModuleApplication {
    public static void main(String[] args) {
        SpringApplication.run(VCSIntegrationModuleApplication.class, args);
    }
}