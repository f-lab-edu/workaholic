package vcs.integration.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import datasource.work.model.entity.WorkProject;
import datasource.work.service.WorkProjectService;
import rabbit.message.queue.ProducerService;
import vcs.integration.exception.type.FailedCloneRepositoryException;
import vcs.integration.service.VCSIntegrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
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
    private final WorkProjectService workProjectService;

    public MessageQueueListener(ObjectMapper objectMapper, VCSIntegrationService vcsIntegrationService, ProducerService producerService, WorkProjectService workProjectService) {
        this.objectMapper = objectMapper;
        this.vcsIntegrationService = vcsIntegrationService;
        this.producerService = producerService;
        this.workProjectService = workProjectService;
    }

    @RabbitListener(queues = "workaholic.vcs", concurrency = "2")
    public void receiveMessageQueue(Message message) throws IOException {
        String token = message.getMessageProperties().getHeader(HttpHeaders.AUTHORIZATION);
        WorkProject createdWorkProject = objectMapper.readValue(message.getBody(), WorkProject.class);
        try {
            String clonedPath = vcsIntegrationService.cloneRepository(createdWorkProject.getId(), createdWorkProject.getRepoUrl(), token);
            workProjectService.setClonedPath(createdWorkProject, clonedPath);
            producerService.sendMessageQueue("kubernetes.build", createdWorkProject);
        } catch (FailedCloneRepositoryException e) {
            log.error("Message processing failed", e);
            workProjectService.failedCloneRepo(createdWorkProject);
            producerService.sendMessageQueue("error.clone", createdWorkProject.getId());
        }
    }
}
