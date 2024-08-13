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
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("workaholic.exchange");
    }

    @Bean
    public Queue kubeQueue() {
        return new Queue("workaholic.kubernetes");
    }

    @Bean
    public Queue vcsQueue() {
        return new Queue("workaholic.vcs");
    }

    @Bean
    public Queue errorQueue() {
        return new Queue("workaholic.error");
    }

    @Bean
    public Binding kubeBindingBuild(TopicExchange topicExchange) {
        return BindingBuilder.bind(kubeQueue()).to(topicExchange).with("kubernetes.build");
    }

    @Bean
    public Binding kubeBindingDeploy(TopicExchange topicExchange) {
        return BindingBuilder.bind(kubeQueue()).to(topicExchange).with("kubernetes.deploy");
    }

    @Bean
    public Binding vcsIntegrationBindingClone(TopicExchange topicExchange) {
        return BindingBuilder.bind(vcsQueue()).to(topicExchange).with("integration.clone");
    }

    @Bean
    public Binding vcsIntegrationBindingBranch(TopicExchange topicExchange) {
        return BindingBuilder.bind(vcsQueue()).to(topicExchange).with("integration.branch");
    }

    @Bean
    public Binding errorBindingClone(TopicExchange topicExchange) {
        return BindingBuilder.bind(errorQueue()).to(topicExchange).with("error.clone");
    }

    @Bean
    public Binding errorBindingBuild(TopicExchange topicExchange) {
        return BindingBuilder.bind(errorQueue()).to(topicExchange).with("error.build");
    }

    @Bean
    public Binding errorBindingDeploy(TopicExchange topicExchange) {
        return BindingBuilder.bind(errorQueue()).to(topicExchange).with("error.deploy");
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
