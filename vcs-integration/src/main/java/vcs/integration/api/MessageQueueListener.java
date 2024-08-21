package vcs.integration.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import datasource.work.model.entity.WorkProject;
import datasource.work.service.WorkProjectService;
import message.queue.error.config.ExceptionStatus;
import message.queue.error.config.exception.ErrorQueueException;
import message.queue.error.service.ErrorQueueProducerService;
import message.queue.kubernetes.service.KubernetesProducerService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    private final ObjectMapper objectMapper;
    private final VCSIntegrationService vcsIntegrationService;
    private final KubernetesProducerService kubernetesProducerService;
    private final ErrorQueueProducerService errorQueueProducerService;
    private final WorkProjectService workProjectService;

    public MessageQueueListener(ObjectMapper objectMapper, VCSIntegrationService vcsIntegrationService, KubernetesProducerService kubernetesProducerService, ErrorQueueProducerService errorQueueProducerService, WorkProjectService workProjectService) {
        this.objectMapper = objectMapper;
        this.vcsIntegrationService = vcsIntegrationService;
        this.kubernetesProducerService = kubernetesProducerService;
        this.errorQueueProducerService = errorQueueProducerService;
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
                                    @Header("transaction_id")String transactionId) {
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

            MessageProperties properties = new MessageProperties();
            properties.setHeader("transaction_id", txId);
            byte[] messageBody = objectMapper.writeValueAsBytes(workProject);
            Message message = new Message(messageBody, properties);
            kubernetesProducerService.sendBuildMessageQueue(message);
        } catch (FailedCloneRepositoryException e) {
            log.error("Clone repository processing failed" , e);
            errorQueueProducerService.sendExceptionMessage(
                    new ErrorQueueException(txId, ExceptionStatus.FAILED_CLONE));
        } catch (FailedCreateDirectory e) {
            log.error("Create directory processing failed" , e);
            errorQueueProducerService.sendExceptionMessage(
                    new ErrorQueueException(txId, ExceptionStatus.FAILED_CREATE));
        } catch (IOException e) {
            log.error("Message queue read processing failed" , e);
            errorQueueProducerService.sendExceptionMessage(
                    new ErrorQueueException(txId, ExceptionStatus.FAILED_READ_MESSAGE_QUEUE));
        }
    }

    private void checkoutRepository(byte[] body, UUID txId) {
        try {
            WorkProject workProject = objectMapper.readValue(body, WorkProject.class);
            vcsIntegrationService.checkoutRepositoryByBranchName(workProject.getClonePath(), workProject.getBranchName());

            MessageProperties properties = new MessageProperties();
            properties.setHeader("transaction_id", txId);
            byte[] messageBody = objectMapper.writeValueAsBytes(workProject);
            Message message = new Message(messageBody, properties);
            kubernetesProducerService.sendBuildMessageQueue(message);
        } catch (FailedCheckoutRepositoryException e) {
            log.error("Checkout repository processing failed", e);
            errorQueueProducerService.sendExceptionMessage(
                    new ErrorQueueException(txId, ExceptionStatus.FAILED_CHECK_OUT));
        } catch (IOException e) {
            log.error("Message queue read processing failed" , e);
            errorQueueProducerService.sendExceptionMessage(
                    new ErrorQueueException(txId, ExceptionStatus.FAILED_READ_MESSAGE_QUEUE));
        }
    }

    private void fetchRepository(byte[] body, String token, UUID txId) {
        try {
            WorkProject workProject = objectMapper.readValue(body, WorkProject.class);
            vcsIntegrationService.fetchRepository(workProject.getClonePath(), token);
        } catch (FailedFetchRepositoryException e) {
            log.error("Fetch repository processing failed", e);
            errorQueueProducerService.sendExceptionMessage(
                    new ErrorQueueException(txId, ExceptionStatus.FAILED_FETCH));
        } catch (IOException e) {
            log.error("Message queue read processing failed" , e);
            errorQueueProducerService.sendExceptionMessage(
                    new ErrorQueueException(txId, ExceptionStatus.FAILED_READ_MESSAGE_QUEUE));
        }
    }
}
