package com.project.kubernetes.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.kubernetes.build.model.ProjectBuild;
import com.project.kubernetes.build.service.ProjectBuildService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class MessageQueueListener {
    private final ObjectMapper objectMapper;
    private final ProjectBuildService buildService;

    public MessageQueueListener(ObjectMapper objectMapper, ProjectBuildService buildService) {
        this.objectMapper = objectMapper;
        this.buildService = buildService;
    }

    @RabbitListener(queues = "workaholic.kubernetes", concurrency = "2")
    public void onMessage(Message message, Channel channel) throws IOException {
        ProjectBuild projectBuild = objectMapper.readValue(message.getBody(), ProjectBuild.class);
        buildService.buildImage(projectBuild);
    }
}
