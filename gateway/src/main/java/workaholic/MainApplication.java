package workaholic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ConfigurationPropertiesScan(basePackages = {"workaholic.config","datasource", "message.queue"})
@EntityScan(basePackages = "datasource")
@EnableJpaRepositories(basePackages = "datasource")
@SpringBootApplication(scanBasePackages = {"message.queue", "datasource", "workaholic"})
public class MainApplication {
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}
