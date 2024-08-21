package message.queue.kubernetes.config;

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
public class KubernetesMessageQueueConfig {
    private static final String KUBE_QUEUE = "workaholic.kubernetes";
    private static final String KUBE_QUEUE_PATTERN = "kubernetes.*";

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("workaholic.exchange");
    }

    @Bean
    public Queue kubeQueue() {
        return new Queue(KUBE_QUEUE);
    }

    @Bean
    public Binding kubernetesBindingBuild(TopicExchange topicExchange) {
        return BindingBuilder.bind(kubeQueue()).to(topicExchange).with(KUBE_QUEUE_PATTERN);
    }
}
