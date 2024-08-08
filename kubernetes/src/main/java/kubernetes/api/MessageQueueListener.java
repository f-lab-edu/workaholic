package kubernetes.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import datasource.work.model.entity.WorkProject;
import datasource.work.model.entity.WorkProjectSetting;
import datasource.work.repository.WorkProjectSettingRepository;
import datasource.work.service.WorkProjectService;
import kubernetes.build.service.ProjectBuildService;
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
    private final ProjectBuildService buildService;
    private final WorkProjectService workProjectService;

    public MessageQueueListener(ObjectMapper objectMapper, ProjectBuildService buildService, WorkProjectService workProjectService) {
        this.objectMapper = objectMapper;
        this.buildService = buildService;
        this.workProjectService = workProjectService;
    }

    @RabbitListener(queues = "workaholic.kubernetes", concurrency = "2")
    public void receiveMessageQueue(Message message) throws IOException {
        String projectId = message.getMessageProperties().getHeader("project-id");
        String projectPath = objectMapper.readValue(message.getBody(), String.class);

        WorkProjectSetting projectSetting = workProjectService.getSettingByWorkProjectId(projectId);
        buildService.buildImage(projectPath, projectSetting);
    }
}
