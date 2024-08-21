package workaholic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ConfigurationPropertiesScan(basePackages = {"workaholic","datasource", "message.queue"})
@EntityScan(basePackages = "datasource")
@EnableJpaRepositories(basePackages = "datasource")
@SpringBootApplication(scanBasePackages = {"workaholic","datasource", "message.queue"})
public class MainApplication {
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}
