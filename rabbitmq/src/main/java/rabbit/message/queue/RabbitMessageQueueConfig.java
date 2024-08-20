package rabbit.message.queue;

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
public class RabbitMessageQueueConfig {
    private static final String KUBE_BUILD_QUEUE = "workaholic.kubernetes.build";
    private static final String KUBE_DEPLOY_QUEUE = "workaholic.kubernetes.deploy";
    private static final String VCS_QUEUE = "workaholic.vcs";
    private static final String ERROR_QUEUE = "workaholic.error";

    private static final String KUBE_BUILD_QUEUE_PATTERN = "workaholic.kubernetes.build.*";
    private static final String KUBE_DEPLOY_QUEUE_PATTERN = "workaholic.kubernetes.deploy.*";
    private static final String VCS_QUEUE_PATTERN = "integration.*";
    private static final String ERROR_QUEUE_PATTERN = "error.*";

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("workaholic.exchange");
    }

    @Bean
    public Queue kubeBuildQueue() {
        return new Queue(KUBE_BUILD_QUEUE);
    }

    @Bean
    public Queue kubeDeployQueue() {
        return new Queue(KUBE_DEPLOY_QUEUE);
    }

    @Bean
    public Queue vcsQueue() {
        return new Queue(VCS_QUEUE);
    }

    @Bean
    public Queue errorQueue() {
        return new Queue(ERROR_QUEUE);
    }

    @Bean
    public Binding kubeBuildBindingBuild(TopicExchange topicExchange) {
        return BindingBuilder.bind(kubeBuildQueue()).to(topicExchange).with(KUBE_BUILD_QUEUE_PATTERN);
    }

    @Bean
    public Binding kubeDeployBindingDeploy(TopicExchange topicExchange) {
        return BindingBuilder.bind(kubeDeployQueue()).to(topicExchange).with(KUBE_DEPLOY_QUEUE_PATTERN);
    }

    @Bean
    public Binding vcsIntegrationBindingClone(TopicExchange topicExchange) {
        return BindingBuilder.bind(vcsQueue()).to(topicExchange).with(VCS_QUEUE_PATTERN);
    }

    @Bean
    public Binding errorBindingClone(TopicExchange topicExchange) {
        return BindingBuilder.bind(errorQueue()).to(topicExchange).with(ERROR_QUEUE_PATTERN);
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
