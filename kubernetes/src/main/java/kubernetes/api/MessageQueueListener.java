package kubernetes.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import datasource.pod.model.entity.ProjectPod;
import datasource.pod.service.ProjectPodService;
import datasource.work.model.entity.WorkProject;
import datasource.work.model.entity.WorkProjectSetting;
import datasource.work.service.WorkProjectService;
import kubernetes.build.service.ProjectBuildService;
import kubernetes.deploy.service.DeployService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import rabbit.message.queue.ProducerService;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
public class MessageQueueListener {
    private final static String DEPLOY_ROUTING_KEY = "kubernetes.deploy";

    private final ObjectMapper objectMapper;
    private final ProjectBuildService buildService;
    private final DeployService deployService;
    private final WorkProjectService workProjectService;
    private final ProducerService producerService;
    private final ProjectPodService projectPodService;

    public MessageQueueListener(ObjectMapper objectMapper, ProjectBuildService buildService, DeployService deployService, WorkProjectService workProjectService, ProducerService producerService, ProjectPodService projectPodService) {
        this.objectMapper = objectMapper;
        this.buildService = buildService;
        this.deployService = deployService;
        this.workProjectService = workProjectService;
        this.producerService = producerService;
        this.projectPodService = projectPodService;
    }

    @RabbitListener(queues = "workaholic.kubernetes", concurrency = "2")
    public void receiveBuildMessageQueue(@Payload byte[] body,
                                         @Header(AmqpHeaders.RECEIVED_ROUTING_KEY)String rotingKey,
                                         @Header("transaction_id")String transactionId) throws IOException {
        UUID txId = UUID.fromString(transactionId);
        if (rotingKey.equals("kubernetes.build")) {
            buildDockerImage(body, txId);
        } else if(rotingKey.equals("kubernetes.deploy")) {
            deployApplication(body, txId);
        }
    }

    private void buildDockerImage(byte[] body, UUID txId) {
        try {
            WorkProject workProject = objectMapper.readValue(body, WorkProject.class);
            WorkProjectSetting projectSetting = workProjectService.getSettingByWorkProjectId(workProject.getId());

            buildService.buildImage(workProject.getClonePath(), projectSetting);

            ProjectPod createdPod = new ProjectPod(workProject.getId());
            projectPodService.createProjectPod(createdPod);
            producerService.sendMessageQueue(DEPLOY_ROUTING_KEY, projectSetting, txId);
        } catch (Exception e) {
            log.error("Build Error");
            producerService.sendExceptionMessage(txId, e);
        }
    }

    public void deployApplication(byte[] body, UUID txId) {
        try {
            WorkProjectSetting projectSetting = objectMapper.readValue(body, WorkProjectSetting.class);
            ProjectPod projectPod = projectPodService.getPodByProjectId(projectSetting.getId());
            String address = deployService.deployApplication(projectPod.getNamespace(), projectPod.getName(),
                    "imageName", projectSetting.getTargetPort(), projectSetting.getNodePort());
            projectPodService.setAccessAddress(projectPod, address);
        } catch (Exception e) {
            log.error("Deploy Error");
            producerService.sendExceptionMessage(txId, e);
        }
    }
}
