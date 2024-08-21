package exception.processing.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import datasource.transaction.model.entity.EventTrace;
import datasource.transaction.service.EventTransactionService;
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
    private final EventTransactionService transactionService;

    public MessageQueueListener(ObjectMapper objectMapper, EventTransactionService transactionService) {
        this.objectMapper = objectMapper;
        this.transactionService = transactionService;
    }

    @RabbitListener(queues = "workaholic.error", concurrency = "2")
    public void receiveErrorMessageQueue(@Payload byte[] body) throws IOException {
        ErrorQueueException exception = objectMapper.readValue(body, ErrorQueueException.class);
        EventTrace trace = new EventTrace(exception.getTransactionId(), exception.getStatus().name() ,stackTraceToString(exception.getStackTrace()));
        transactionService.addTrace(trace);
    }

    private String stackTraceToString(StackTraceElement[] stackTraceElements) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : stackTraceElements) {
            sb.append(element.toString()).append("\n");
        }
        return sb.toString();
    }

}
