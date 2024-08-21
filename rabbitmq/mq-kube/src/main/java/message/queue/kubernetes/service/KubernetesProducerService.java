package message.queue.kubernetes.service;

import lombok.extern.slf4j.Slf4j;
import message.queue.kubernetes.config.KubernetesMessageQueueProperties;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KubernetesProducerService {
    private static final String BUILD_ROUTING_KEY = "kubernetes.build";
    private static final String DEPLOY_ROUTING_KEY = "kubernetes.deploy";
    private final KubernetesMessageQueueProperties properties;
    private final RabbitTemplate rabbitTemplate;

    public KubernetesProducerService(KubernetesMessageQueueProperties properties, RabbitTemplate rabbitTemplate) {
        this.properties = properties;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendBuildMessageQueue(Message message) {
        rabbitTemplate.convertAndSend(properties.getExchange(), BUILD_ROUTING_KEY, message);
    }

    public void sendDeployMessageQueue(Message message) {
        rabbitTemplate.convertAndSend(properties.getExchange(), DEPLOY_ROUTING_KEY, message);
    }
}
