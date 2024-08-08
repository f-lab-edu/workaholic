package com.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ConfigurationPropertiesScan({"datasource.work", "rabbit.message.queue"})
@SpringBootApplication(scanBasePackages = {"com.project.work", "datasource.work", "rabbit.message.queue"})
@EnableJpaRepositories(basePackages = "datasource.work.repository")
public class WorkProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(WorkProjectApplication.class, args);
    }
}
