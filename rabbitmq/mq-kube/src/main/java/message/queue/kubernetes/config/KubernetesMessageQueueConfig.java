package message.queue.kubernetes.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KubernetesMessageQueueConfig {
    private static final String KUBE_QUEUE = "workaholic.kubernetes";
    private static final String KUBE_QUEUE_PATTERN = "kubernetes.*";
    private final KubernetesMessageQueueProperties properties;

    public KubernetesMessageQueueConfig(KubernetesMessageQueueProperties properties) {
        this.properties = properties;
    }

    @Bean
    public TopicExchange kubeTopicExchange() {
        return new TopicExchange(properties.getExchange());
    }

    @Bean
    public Queue kubeQueue() {
        return new Queue(KUBE_QUEUE);
    }

    @Bean
    public Binding kubernetesBindingBuild() {
        return BindingBuilder.bind(kubeQueue()).to(kubeTopicExchange()).with(KUBE_QUEUE_PATTERN);
    }
}
