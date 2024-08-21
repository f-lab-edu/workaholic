package kubernetes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ConfigurationPropertiesScan({"message.queue", "kubernetes"})
@EntityScan(basePackages = {"datasource"})
@EnableJpaRepositories(basePackages = {"datasource"})
@SpringBootApplication(scanBasePackages = {"datasource", "message.queue", "kubernetes"})
public class KubernetesModuleApplication {
    public static void main(String[] args) {
        SpringApplication.run(KubernetesModuleApplication.class, args);
    }
}