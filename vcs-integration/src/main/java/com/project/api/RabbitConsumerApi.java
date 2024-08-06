package com.project.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RabbitConsumerApi {
    @RabbitListener(queues = "workaholic.queue", concurrency = "2")
    public void consumeMessage(Message msg) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String body = new String(msg.getBody());

        log.info("body()={}", body);
    }
}
