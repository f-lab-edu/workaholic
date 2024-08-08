package vcs.integration.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.datasource.work.model.entity.WorkProject;
import rabbit.message.queue.ProducerService;
import vcs.integration.exception.type.FailedCloneRepositoryException;
import vcs.integration.service.VCSIntegrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class MessageQueueListener {
    private final ObjectMapper objectMapper;
    private final VCSIntegrationService vcsIntegrationService;
    private final ProducerService producerService;

    public MessageQueueListener(ObjectMapper objectMapper, VCSIntegrationService vcsIntegrationService, ProducerService producerService) {
        this.objectMapper = objectMapper;
        this.vcsIntegrationService = vcsIntegrationService;
        this.producerService = producerService;
    }

    @RabbitListener(queues = "workaholic.vcs", concurrency = "2")
    public void receiveMessageQueue(Message message) throws IOException {
        String token = message.getMessageProperties().getHeader(HttpHeaders.AUTHORIZATION);
        WorkProject createdWorkProject = objectMapper.readValue(message.getBody(), WorkProject.class);
        try {
            String projectPath = vcsIntegrationService.cloneRepository(createdWorkProject.getId(), createdWorkProject.getRepoUrl(), token);
            producerService.sendMessageQueue("kubernetes.build", projectPath);
        } catch (FailedCloneRepositoryException e) {
            log.error("Message processing failed", e);
            producerService.sendMessageQueue("error.clone", createdWorkProject.getId());
        }
    }

    private void handleRetry(Message message) {
        MessageProperties messageProperties = message.getMessageProperties();
    }
}
