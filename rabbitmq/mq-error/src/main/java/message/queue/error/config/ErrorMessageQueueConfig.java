package message.queue.error.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ErrorMessageQueueConfig {
    private static final String ERROR_QUEUE = "workaholic.error";
    private static final String ERROR_QUEUE_PATTERN = "error";
    private final ErrorMessageQueueProperties properties;

    public ErrorMessageQueueConfig(ErrorMessageQueueProperties properties) {
        this.properties = properties;
    }

    @Bean
    public TopicExchange errorTopicExchange() {
        return new TopicExchange(properties.getExchange());
    }

    @Bean
    public Queue errorQueue() {
        return new Queue(ERROR_QUEUE);
    }

    @Bean
    public Binding errorBindingClone() {
        return BindingBuilder.bind(errorQueue()).to(errorTopicExchange()).with(ERROR_QUEUE_PATTERN);
    }
}
