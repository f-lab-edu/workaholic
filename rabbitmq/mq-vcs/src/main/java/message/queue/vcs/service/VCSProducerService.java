package message.queue.vcs.service;

import lombok.extern.slf4j.Slf4j;
import message.queue.vcs.config.VCSMessageQueueProperties;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class VCSProducerService {
    private static final String CLONE_ROUTING_KEY = "integration.clone";
    private static final String CHECKOUT_ROUTING_KEY = "integration.checkout";
    private static final String FETCH_ROUTING_KEY = "integration.fetch";
    private final VCSMessageQueueProperties properties;
    private final RabbitTemplate rabbitTemplate;

    public VCSProducerService(VCSMessageQueueProperties properties, RabbitTemplate rabbitTemplate) {
        this.properties = properties;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendCloneMessageQueue(Message message) {
        rabbitTemplate.convertAndSend(properties.getExchange(), CLONE_ROUTING_KEY, message);
    }

    public void sendCheckoutMessageQueue(Message message) {
        rabbitTemplate.convertAndSend(properties.getExchange(), CHECKOUT_ROUTING_KEY, message);
    }

    public void sendFetchMessageQueue(Message message) {
        rabbitTemplate.convertAndSend(properties.getExchange(), FETCH_ROUTING_KEY, message);
    }
}
