package workaholic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ConfigurationPropertiesScan(basePackages = {"datasource.work", "rabbit.message.queue"})
@EntityScan(basePackages = "datasource.work.model.entity")
@EnableJpaRepositories(basePackages = "datasource.work.repository")
@SpringBootApplication(scanBasePackages = {"rabbit.message.queue", "datasource.work", "workaholic"})
public class MainApplication {
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}
