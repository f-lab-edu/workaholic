package com.project.config.rabbit;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMessageQueueConfig {
    @Bean
    public Queue kubeQueue() {
        return new Queue("workaholic.kubernetes");
    }

    @Bean
    public Queue vcsQueue() {
        return new Queue("workaholic.vcs");
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("workaholic.exchange");
    }

    @Bean
    public Binding kubeBinding(Queue kubeQueue, DirectExchange directExchange) {
        return BindingBuilder
                .bind(kubeQueue).to(directExchange).with("kubernetes");

    }

    @Bean
    public Binding vcsBinding(Queue vcsQueue, DirectExchange directExchange) {
        return BindingBuilder
                .bind(vcsQueue).to(directExchange).with("vcs-integration");

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
