package exception.processing.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import message.queue.error.config.exception.ErrorQueueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Slf4j
@Controller
public class MessageQueueListener {
    private final ObjectMapper objectMapper;

    public MessageQueueListener(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "workaholic.error", concurrency = "2")
    public void receiveErrorMessageQueue(@Payload byte[] body) throws IOException {
        ErrorQueueException exception = objectMapper.readValue(body, ErrorQueueException.class);

        System.out.println(exception.getClass());
    }
}
