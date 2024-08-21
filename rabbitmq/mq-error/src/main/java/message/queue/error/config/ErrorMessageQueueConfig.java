package message.queue.error.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ErrorMessageQueueConfig {
    private static final String ERROR_QUEUE = "workaholic.error";
    private static final String ERROR_QUEUE_PATTERN = "error";

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("workaholic.exchange");
    }

    @Bean
    public Queue errorQueue() {
        return new Queue(ERROR_QUEUE);
    }

    @Bean
    public Binding errorBindingClone(TopicExchange topicExchange) {
        return BindingBuilder.bind(errorQueue()).to(topicExchange).with(ERROR_QUEUE_PATTERN);
    }
}
