package com.project.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.exception.type.FailedCloneRepositoryException;
import com.project.github.service.GithubService;
import com.project.gitlab.service.GitlabService;
import common.model.WorkProjectRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class RabbitConsumerApi {
    private final GithubService githubService;
    private final GitlabService gitlabService;

    public RabbitConsumerApi(GithubService githubService, GitlabService gitlabService) {
        this.githubService = githubService;
        this.gitlabService = gitlabService;
    }

    @RabbitListener(queues = "workaholic.queue", concurrency = "2")
    public void consumeMessage(Message msg) throws JsonProcessingException {
        String authHeader = msg.getMessageProperties().getHeader("Authorization");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            WorkProjectRequestDto dto = objectMapper.readValue(msg.getBody(), WorkProjectRequestDto.class);
            switch (dto.getVendor()) {
                case GITHUB -> githubService.cloneRepository(dto.getId(), dto.getRepositoryUrl(), authHeader);
                case GITLAB -> gitlabService.cloneRepository(dto.getId(), dto.getRepositoryUrl(), authHeader);
            }
        } catch (IOException | FailedCloneRepositoryException e) {
            throw new RuntimeException(e);
        }
    }
}
