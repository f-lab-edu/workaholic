package rabbit.message.queue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProducerService {
    private final RabbitTemplate rabbitTemplate;
    private final RabbitMessageQueueProperties properties;

    public ProducerService(RabbitTemplate rabbitTemplate, RabbitMessageQueueProperties properties) {
        this.rabbitTemplate = rabbitTemplate;
        this.properties = properties;
    }

    public void sendMessageQueue(String routingKey, Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] messageBody = objectMapper.writeValueAsBytes(obj);
        Message messageQueueMessage = new Message(messageBody);

        rabbitTemplate.convertAndSend(properties.getExchange(), routingKey, messageQueueMessage);
    }

    public void sendMessageQueue(String routingKey, Object obj, MessageProperties messageProperties) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] messageBody = objectMapper.writeValueAsBytes(obj);
        Message messageQueueMessage = new Message(messageBody, messageProperties);

        rabbitTemplate.convertAndSend(properties.getExchange(), routingKey, messageQueueMessage);
    }
}
