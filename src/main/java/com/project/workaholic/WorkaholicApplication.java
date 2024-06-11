package com.project.workaholic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class WorkaholicApplication {
    public static void main(String[] args) {
        SpringApplication.run(WorkaholicApplication.class, args);
    }

}
