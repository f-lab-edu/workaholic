package vcs.integration.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import datasource.work.model.entity.WorkProject;
import datasource.work.service.WorkProjectService;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import rabbit.message.queue.ProducerService;
import vcs.integration.exception.type.FailedCheckoutRepositoryException;
import vcs.integration.exception.type.FailedCloneRepositoryException;
import vcs.integration.exception.type.FailedCreateDirectory;
import vcs.integration.exception.type.FailedFetchRepositoryException;
import vcs.integration.exception.type.FailedGetBranchesException;
import vcs.integration.service.VCSIntegrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

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

    @GetMapping("/branch")
    public ResponseEntity<List<String>> getBranch(
            @RequestParam("id") String projectId) {
        try {
            WorkProject workProject = workProjectService.getWorkProjectById(projectId);
            List<String> branches = vcsIntegrationService.getRepositoryBranches(workProject.getRepoUrl());
            return ResponseEntity.status(HttpStatus.OK).body(branches);
        } catch (FailedGetBranchesException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RabbitListener(queues = "workaholic.vcs", concurrency = "2")
    public void receiveMessageQueue(Message message, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey) throws IOException {
        String token = message.getMessageProperties().getHeader(HttpHeaders.AUTHORIZATION);
        WorkProject workProject = objectMapper.readValue(message.getBody(), WorkProject.class);

        switch (routingKey) {
            case "workaholic.vcs.clone" -> cloningRepository(workProject, token);
            case "workaholic.vcs.checkout" -> checkoutRepository(workProject, workProject.getBranchName());
            case "workaholic.vcs.fetch" -> fetchRepository(workProject, token);
        }
    }

    private void cloningRepository(WorkProject newWorkProject, String token) {
        try {
            String clonedPath = vcsIntegrationService.cloneRepository(newWorkProject.getId(), newWorkProject.getRepoUrl(), token);
            workProjectService.setClonedPath(newWorkProject, clonedPath);
            producerService.sendMessageQueue("kubernetes.build", newWorkProject);
        } catch (FailedCloneRepositoryException e) {
            log.error("Clone repository processing failed" , e);
            workProjectService.failedCloneRepo(newWorkProject);
            producerService.sendMessageQueue("workaholic.error.clone", newWorkProject.getId());
        } catch (FailedCreateDirectory e) {
            log.error("Create directory processing failed" , e);
            producerService.sendMessageQueue("workaholic.error.create", newWorkProject.getClonePath());
        }
    }

    private void checkoutRepository(WorkProject workProject, String branchName) {
        try {
            vcsIntegrationService.checkoutRepositoryByBranchName(workProject.getClonePath(), branchName);
        } catch (FailedCheckoutRepositoryException e) {
            log.error("Checkout repository processing failed", e);
            producerService.sendMessageQueue("workaholic.error.checkout", workProject);
        }
    }

    private void fetchRepository(WorkProject workProject, String token) {
        try {
            vcsIntegrationService.fetchRepository(workProject.getRepoUrl(), token);
        } catch (FailedFetchRepositoryException e) {
            log.error("Fetch repository processing failed", e);
            producerService.sendMessageQueue("workaholic.error.fetch", workProject);
        }
    }
}
