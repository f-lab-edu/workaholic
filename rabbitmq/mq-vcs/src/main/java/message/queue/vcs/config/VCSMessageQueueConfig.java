package message.queue.vcs.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VCSMessageQueueConfig {
    private static final String VCS_QUEUE = "workaholic.vcs";
    private static final String VCS_QUEUE_PATTERN = "integration.*";
    private final VCSMessageQueueProperties properties;

    public VCSMessageQueueConfig(VCSMessageQueueProperties properties) {
        this.properties = properties;
    }

    @Bean
    public TopicExchange vcsTopicExchange() {
        return new TopicExchange(properties.getExchange());
    }

    @Bean
    public Queue vcsQueue() {
        return new Queue(VCS_QUEUE);
    }

    @Bean
    public Binding vcsIntegrationBindingClone() {
        return BindingBuilder.bind(vcsQueue()).to(vcsTopicExchange()).with(VCS_QUEUE_PATTERN);
    }
}
