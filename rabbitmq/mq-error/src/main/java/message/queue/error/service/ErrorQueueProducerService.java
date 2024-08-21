package message.queue.error.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import message.queue.error.config.ErrorMessageQueueProperties;
import message.queue.error.config.exception.ErrorQueueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class ErrorQueueProducerService {
    private final RabbitTemplate rabbitTemplate;
    private final ErrorMessageQueueProperties properties;

    public ErrorQueueProducerService(RabbitTemplate rabbitTemplate, ErrorMessageQueueProperties properties) {
        this.rabbitTemplate = rabbitTemplate;
        this.properties = properties;
    }

    public void sendExceptionMessage(ErrorQueueException exception) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            byte[] body = objectMapper.writeValueAsBytes(exception);
            Message messageQueueMessage = new Message(body);
            rabbitTemplate.convertAndSend(properties.getExchange(), "error", messageQueueMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
