package exception.processing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ConfigurationPropertiesScan({"message.queue"})
@EntityScan(basePackages = "datasource")
@EnableJpaRepositories(basePackages = "datasource")
@SpringBootApplication(scanBasePackages = {"message.queue", "datasource", "rabbit.message.queue"})
public class ExceptionProcessingApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExceptionProcessingApplication.class, args);
    }
}