package workaholic.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import datasource.transaction.service.EventTransactionService;
import message.queue.vcs.service.VCSProducerService;
import org.springframework.amqp.core.Message;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import workaholic.config.response.ApiResponse;
import datasource.work.model.entity.WorkProject;
import datasource.work.model.entity.WorkProjectSetting;
import datasource.work.model.enumeration.ProjectStatus;
import workaholic.model.WorkaholicRequestDTO;
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
    private final ObjectMapper objectMapper;
    private final WorkProjectService workProjectService;
    private final VCSProducerService producerService;
    private final EventTransactionService transactionService;
    private final RestTemplate vcsApplicationRestTemplate;

    public WorkaholicApi(ObjectMapper objectMapper, WorkProjectService workProjectService, VCSProducerService producerService, EventTransactionService transactionService, RestTemplate vcsApplicationRestTemplate) {
        this.objectMapper = objectMapper;
        this.workProjectService = workProjectService;
        this.producerService = producerService;
        this.transactionService = transactionService;
        this.vcsApplicationRestTemplate = vcsApplicationRestTemplate;
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<WorkaholicRequestDTO>> createWorkProject(
            final @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            final @Valid @RequestBody WorkaholicRequestDTO dto) throws JsonProcessingException {
        MessageProperties properties = new MessageProperties();
        properties.setHeader("transaction_id", transactionService.createTransaction());
        properties.setHeader(HttpHeaders.AUTHORIZATION, authorizationHeader);

        WorkProject workProject = new WorkProject(dto.getId(), dto.getRepositoryUrl(), ProjectStatus.CREATE);
        WorkProjectSetting projectSetting = new WorkProjectSetting(workProject.getId(), dto.getBuildType(), dto.getJavaVersion(), dto.getTargetPort(), dto.getNodePort(), dto.getWorkDirectory(), dto.getEnvVariables(), dto.getArgs());
        workProjectService.createWorkProject(workProject, projectSetting);

        byte[] messageBody = objectMapper.writeValueAsBytes(workProject);
        Message message = new Message(messageBody, properties);
        producerService.sendCloneMessageQueue(message);
        return ApiResponse.success(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<WorkaholicResponseDTO>> getWorkProjectConfigById(
            final @PathVariable("id") String projectId) {
        WorkProject workProject = workProjectService.getWorkProjectById(projectId);
        WorkProjectSetting setting = workProjectService.getSettingByWorkProjectId(workProject.getId());

        WorkaholicResponseDTO response = new WorkaholicResponseDTO(workProject.getId(), setting.getJavaVersion(), setting.getBuildType(), setting.getWorkDirectory(), setting.getTargetPort(), setting.getNodePort(), setting.getEnvVariables(), setting.getExecuteParameters());
        return ApiResponse.success(response);
    }

    @GetMapping("/{id}/branch")
    public ResponseEntity<ApiResponse<List<String>>> getBranchesByWorkProjectId(
            final @PathVariable("id") String projectId) {
        String branchCallUri = "/branch?id=" + projectId;

        try {
            ResponseEntity<List<String>> response =
                    vcsApplicationRestTemplate.exchange(branchCallUri, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
            return ApiResponse.success(response.getBody());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> updateWorkProjectConfigById(
            final @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            final @PathVariable("id") String projectId,
            final @RequestBody WorkaholicUpdateDTO dto) {
        WorkProjectSetting existingSetting = workProjectService.getSettingByWorkProjectId(projectId);
        WorkProjectSetting updatedSetting = new WorkProjectSetting(dto.getBuildType(), dto.getJavaVersion(), dto.getTargetPort(), dto.getNodePort(), dto.getWorkDirectory(), dto.getEnvVariables(), dto.getArgs());

        workProjectService.updateWorkProject(existingSetting, updatedSetting);
        return ApiResponse.success(existingSetting.getId());
    }

    @PatchMapping("/{id}/checkout")
    public ResponseEntity<ApiResponse<String>> checkoutBranch(
            final @PathVariable("id") String projectId,
            final @RequestParam("branch") String branchName) throws JsonProcessingException {
        MessageProperties properties = new MessageProperties();
        properties.setHeader("transaction_id", transactionService.createTransaction());
        WorkProject workProject = workProjectService.getWorkProjectById(projectId);

        byte[] messageBody = objectMapper.writeValueAsBytes(workProject);
        Message message = new Message(messageBody, properties);
        workProjectService.changeBranch(workProject, branchName);
        producerService.sendCheckoutMessageQueue(message);
        return ApiResponse.success(branchName);
    }

    @PatchMapping("/{id}/fetch")
    public ResponseEntity<Void> fetchBranch(
            final @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            final @PathVariable("id") String projectId) throws JsonProcessingException {
        MessageProperties properties = new MessageProperties();
        properties.setHeader("transaction_id", transactionService.createTransaction());
        properties.setHeader(HttpHeaders.AUTHORIZATION, authorizationHeader);

        WorkProject workProject = workProjectService.getWorkProjectById(projectId);
        byte[] messageBody = objectMapper.writeValueAsBytes(workProject);
        Message message = new Message(messageBody, properties);

        producerService.sendFetchMessageQueue(message);
        return ApiResponse.success();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkProjectById(
            final @PathVariable("id") String projectId) {
        WorkProject deletedWorkProject = workProjectService.getWorkProjectById(projectId);
        workProjectService.deleteWorkProject(deletedWorkProject);

        return ApiResponse.success();
    }
}
