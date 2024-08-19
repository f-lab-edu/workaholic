package rabbit.message.queue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProducerService {
    private final RabbitTemplate rabbitTemplate;
    private final RabbitMessageQueueProperties properties;

    public ProducerService(RabbitTemplate rabbitTemplate, RabbitMessageQueueProperties properties) {
        this.rabbitTemplate = rabbitTemplate;
        this.properties = properties;
    }

    public void sendMessageQueue(String routingKey, Object obj) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            byte[] messageBody = objectMapper.writeValueAsBytes(obj);
            Message messageQueueMessage = new Message(messageBody);
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
