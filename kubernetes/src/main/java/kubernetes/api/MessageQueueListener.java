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
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import rabbit.message.queue.ProducerService;

import java.io.IOException;

@Slf4j
@Component
public class MessageQueueListener {
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

    @RabbitListener(queues = "workaholic.kubernetes.build", concurrency = "2")
    public void receiveBuildMessageQueue(Message message) throws IOException {
        WorkProject workProject = objectMapper.readValue(message.getBody(), WorkProject.class);
        WorkProjectSetting projectSetting = workProjectService.getSettingByWorkProjectId(workProject.getId());

        try {
            buildService.buildImage(workProject.getClonePath(), projectSetting);
            ProjectPod createdPod = new ProjectPod(workProject.getId());
            projectPodService.createProjectPod(createdPod);
            producerService.sendMessageQueue("workaholic.kubernetes.deploy", projectSetting);
        } catch (Exception e) {
            log.error("Build Error");
            producerService.sendMessageQueue("workaholic.error.build", workProject);
        }
    }

    @RabbitListener(queues = "workaholic.kubernetes.deploy", concurrency = "2")
    public void receiveDeployMessageQueue(Message message) throws IOException {
        WorkProjectSetting projectSetting = objectMapper.readValue(message.getBody(), WorkProjectSetting.class);
        ProjectPod projectPod = projectPodService.getPodByProjectId(projectSetting.getId());

        try {
            String address = deployService.deployApplication(projectPod.getNamespace(), projectPod.getName(),
                    "imageName", projectSetting.getTargetPort(), projectSetting.getNodePort());
            projectPodService.setAccessAddress(projectPod, address);
        } catch (Exception e) {
            log.error("Deploy Error");
            producerService.sendMessageQueue("workaholic.error.deploy", projectPod);
        }
    }
}
