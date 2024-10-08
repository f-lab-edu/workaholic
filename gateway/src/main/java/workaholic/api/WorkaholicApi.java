package workaholic.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import workaholic.config.response.ApiResponse;
import datasource.work.model.entity.WorkProject;
import datasource.work.model.entity.WorkProjectSetting;
import datasource.work.model.enumeration.ProjectStatus;
import workaholic.model.WorkaholicRequestDTO;
import rabbit.message.queue.ProducerService;
import workaholic.model.WorkaholicResponseDTO;
import workaholic.model.WorkaholicUpdateDTO;
import datasource.work.service.WorkProjectService;
import jakarta.validation.Valid;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/project")
public class WorkaholicApi {
    private final static String VCS_ROUTING_KEY = "integration.clone";
    private final static String KUBE_ROUTING_KEY = "kubernetes";

    private final WorkProjectService workProjectService;
    private final ProducerService producerService;

    public WorkaholicApi(WorkProjectService workProjectService, ProducerService producerService) {
        this.workProjectService = workProjectService;
        this.producerService = producerService;
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<WorkaholicRequestDTO>> createWorkProject(
            final @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            final @Valid @RequestBody WorkaholicRequestDTO dto) {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setHeader(HttpHeaders.AUTHORIZATION, authorizationHeader);

        WorkProject createdWorkProject = new WorkProject(dto.getId(), dto.getRepositoryUrl(), ProjectStatus.CREATE);
        WorkProjectSetting createdSetting = new WorkProjectSetting(createdWorkProject.getId(), dto.getBuildType(), dto.getJavaVersion(), dto.getPort(), dto.getWorkDirectory(), dto.getEnvVariables(), dto.getArgs());
        workProjectService.createWorkProject(createdWorkProject, createdSetting);

        try {
            producerService.sendMessageQueue(VCS_ROUTING_KEY, createdWorkProject, messageProperties);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return ApiResponse.success(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<WorkaholicResponseDTO>> getWorkProjectConfigById(
            final @PathVariable("id") String projectId) {
        WorkProject workProject = workProjectService.getWorkProjectById(projectId);
        WorkProjectSetting setting = workProjectService.getSettingByWorkProjectId(workProject.getId());

        WorkaholicResponseDTO response = new WorkaholicResponseDTO(workProject.getId(), setting.getJavaVersion(), setting.getBuildType(), setting.getWorkDirectory(), setting.getPort(), setting.getEnvVariables(), setting.getExecuteParameters());
        return ApiResponse.success(response);
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<String>>> getWorkProjectConfig() {
        List<String> response = workProjectService.getAllWorkProject()
                .stream().map(WorkProject::getId).toList();
        return ApiResponse.success(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> updateWorkProjectConfigById(
            final @PathVariable("id") String projectId,
            final @RequestBody WorkaholicUpdateDTO dto) {
        WorkProjectSetting existingSetting = workProjectService.getSettingByWorkProjectId(projectId);
        WorkProjectSetting updatedSetting = new WorkProjectSetting(dto.getBuildType(), dto.getJavaVersion(), dto.getPort(), dto.getWorkDirectory(), dto.getEnvVariables(), dto.getArgs());

        workProjectService.updateWorkProject(existingSetting, updatedSetting);
        return ApiResponse.success(existingSetting.getId());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkProjectById(
            final @PathVariable("id") String projectId) {
        WorkProject deletedWorkProject = workProjectService.getWorkProjectById(projectId);
        workProjectService.deleteWorkProject(deletedWorkProject);

        return ApiResponse.success();
    }
}
