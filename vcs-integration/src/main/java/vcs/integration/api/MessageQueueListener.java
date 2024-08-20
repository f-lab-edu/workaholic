package vcs.integration.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import datasource.work.model.entity.WorkProject;
import datasource.work.service.WorkProjectService;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
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
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
public class MessageQueueListener {
    private final static String BUILD_ROUTING_KEY = "kubernetes.build";

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
            List<String> branches = vcsIntegrationService.getRepositoryBranches(workProject.getClonePath());
            return ResponseEntity.status(HttpStatus.OK).body(branches);
        } catch (FailedGetBranchesException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RabbitListener(queues = "workaholic.vcs", concurrency = "2")
    public void receiveMessageQueue(@Payload byte[] body,
                                    @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey,
                                    @Header(HttpHeaders.AUTHORIZATION) String token,
                                    @Header("transaction_id")String transactionId) throws IOException {
        UUID txId = UUID.fromString(transactionId);
        switch (routingKey) {
            case "integration.clone" -> cloningRepository(body, token, txId);
            case "integration.checkout" -> checkoutRepository(body, txId);
            case "integration.fetch" -> fetchRepository(body, token, txId);
        }
    }

    private void cloningRepository(byte[] body, String token, UUID txId) {
        try {
            WorkProject workProject = objectMapper.readValue(body, WorkProject.class);
            String clonedPath = vcsIntegrationService.cloneRepository(workProject.getId(), workProject.getRepoUrl(), token);
            workProjectService.setClonedPath(workProject, clonedPath);
            producerService.sendMessageQueue(BUILD_ROUTING_KEY, workProject, txId);
        } catch (FailedCloneRepositoryException e) {
            log.error("Clone repository processing failed" , e);
            producerService.sendExceptionMessage(txId, e);
        } catch (FailedCreateDirectory e) {
            log.error("Create directory processing failed" , e);
            producerService.sendExceptionMessage(txId, e);
        } catch (IOException e) {
            log.error("Message queue read processing failed" , e);
            producerService.sendExceptionMessage(txId, e);
        }
    }

    private void checkoutRepository(byte[] body, UUID txId) {
        try {
            WorkProject workProject = objectMapper.readValue(body, WorkProject.class);
            vcsIntegrationService.checkoutRepositoryByBranchName(workProject.getClonePath(), workProject.getBranchName());
            producerService.sendMessageQueue(BUILD_ROUTING_KEY, workProject, txId);
        } catch (FailedCheckoutRepositoryException e) {
            log.error("Checkout repository processing failed", e);
            producerService.sendExceptionMessage(txId, e);
        } catch (IOException e) {
            log.error("Message queue read processing failed" , e);
            producerService.sendExceptionMessage(txId, e);
        }
    }

    private void fetchRepository(byte[] body, String token, UUID txId) {
        try {
            WorkProject workProject = objectMapper.readValue(body, WorkProject.class);
            vcsIntegrationService.fetchRepository(workProject.getClonePath(), token);
        } catch (FailedFetchRepositoryException e) {
            log.error("Fetch repository processing failed", e);
            producerService.sendExceptionMessage(txId, e);
        } catch (IOException e) {
            log.error("Message queue read processing failed" , e);
            producerService.sendExceptionMessage(txId, e);
        }
    }
}
