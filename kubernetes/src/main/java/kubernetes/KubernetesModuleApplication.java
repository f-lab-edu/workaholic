package kubernetes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ConfigurationPropertiesScan({"rabbit.message.queue", "kubernetes"})
@EntityScan(basePackages = "datasource.work.model.entity")
@EnableJpaRepositories(basePackages = "datasource.work.repository")
@SpringBootApplication(scanBasePackages = {"datasource.work", "rabbit.message.queue", "kubernetes"})
public class KubernetesModuleApplication {
    public static void main(String[] args) {
        SpringApplication.run(KubernetesModuleApplication.class, args);
    }
}