package vcs.integration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ConfigurationPropertiesScan({"message.queue", "vcs"})
@EntityScan(basePackages = "datasource")
@EnableJpaRepositories(basePackages = "datasource")
@SpringBootApplication(scanBasePackages = {"datasource", "message.queue", "vcs"})
public class VCSIntegrationModuleApplication {
    public static void main(String[] args) {
        SpringApplication.run(VCSIntegrationModuleApplication.class, args);
    }
}