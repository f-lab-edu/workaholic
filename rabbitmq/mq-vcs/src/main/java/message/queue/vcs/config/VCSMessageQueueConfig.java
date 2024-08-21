package message.queue.vcs.config;

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
public class VCSMessageQueueConfig {
    private static final String VCS_QUEUE = "workaholic.vcs";
    private static final String VCS_QUEUE_PATTERN = "integration.*";

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("workaholic.exchange");
    }

    @Bean
    public Queue vcsQueue() {
        return new Queue(VCS_QUEUE);
    }

    @Bean
    public Binding vcsIntegrationBindingClone(TopicExchange topicExchange) {
        return BindingBuilder.bind(vcsQueue()).to(topicExchange).with(VCS_QUEUE_PATTERN);
    }
}
