package workaholic.config;

import datasource.work.repository.WorkProjectRepository;
import datasource.work.repository.WorkProjectSettingRepository;
import datasource.work.service.WorkProjectService;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationConfig {
    private final ApplicationProperties properties;
    private final WorkProjectRepository workProjectRepository;
    private final WorkProjectSettingRepository projectSettingRepository;

    public ApplicationConfig(ApplicationProperties properties, WorkProjectRepository workProjectRepository, WorkProjectSettingRepository projectSettingRepository) {
        this.properties = properties;
        this.workProjectRepository = workProjectRepository;
        this.projectSettingRepository = projectSettingRepository;
    }

    @Bean
    public WorkProjectService workProjectService() {
        return new WorkProjectService(this.workProjectRepository, this.projectSettingRepository);
    }

    @Bean
    public RestTemplate vcsApplicationRestTemplate(RestTemplateBuilder builder) {
        return builder.rootUri(properties.getVcsApp()).build();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }

    @Bean
    public MessageConverter messageConverter() {
        return new SimpleMessageConverter();
    }
}
