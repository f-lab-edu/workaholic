package message.queue.kubernetes.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import message.queue.kubernetes.config.KubernetesMessageQueueProperties;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class KubernetesProducerService {
    private static final String TRANSACTION_HEADER = "transaction_id";
    private final RabbitTemplate rabbitTemplate;
    private final KubernetesMessageQueueProperties properties;

    public KubernetesProducerService(RabbitTemplate rabbitTemplate, KubernetesMessageQueueProperties properties) {
        this.rabbitTemplate = rabbitTemplate;
        this.properties = properties;
    }

    public void sendMessageQueue(String routingKey, Object obj, UUID transactionId) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            byte[] messageBody = objectMapper.writeValueAsBytes(obj);
            Message messageQueueMessage = MessageBuilder.withBody(messageBody)
                    .setHeader(TRANSACTION_HEADER, transactionId)
                    .build();
            rabbitTemplate.convertAndSend(properties.getExchange(), routingKey, messageQueueMessage);
        } catch (JsonProcessingException e) {
            log.error("Message processing failed", e);
        }
    }

    public void sendMessageQueue(String routingKey, Object obj, MessageProperties messageProperties) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            byte[] messageBody = objectMapper.writeValueAsBytes(obj);
            Message messageQueueMessage = new Message(messageBody, messageProperties);
            rabbitTemplate.convertAndSend(properties.getExchange(), routingKey, messageQueueMessage);
        } catch (JsonProcessingException e) {
            log.error("Message processing failed", e);
        }
    }
}
