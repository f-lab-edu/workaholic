package com.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan("rabbit.message.queue")
@SpringBootApplication(scanBasePackages = {"com.project.work", "com.project.datasource.work", "rabbit.message.queue"})
public class WorkProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(WorkProjectApplication.class, args);
    }
}
