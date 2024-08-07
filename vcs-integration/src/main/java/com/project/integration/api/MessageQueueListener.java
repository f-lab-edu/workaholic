package com.project.integration.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.integration.service.VCSIntegrationService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
public class MessageQueueListener {
    private final ObjectMapper objectMapper;
    private final VCSIntegrationService vcsIntegrationService;

    public MessageQueueListener(ObjectMapper objectMapper, VCSIntegrationService vcsIntegrationService) {
        this.objectMapper = objectMapper;
        this.vcsIntegrationService = vcsIntegrationService;
    }

    @RabbitListener(queues = "workaholic.vcs", concurrency = "2")
    public void onMessage(Message message, Channel channel) throws Exception {
        String token = message.getMessageProperties().getHeader(HttpHeaders.AUTHORIZATION);
        try {
            Map<String, String> dto = objectMapper.readValue(message.getBody(), new TypeReference<Map<String, String>>() {});
            String projectPath = vcsIntegrationService.cloneRepository(dto.get("id"), dto.get("repositoryUrl"), token);

            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            log.error("Message processing failed", e);

            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
        }
    }
}
