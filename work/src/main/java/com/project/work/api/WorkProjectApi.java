package com.project.work.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.config.response.ApiResponse;
import com.project.work.service.ProducerService;
import common.model.WorkProjectRequestDto;
import com.project.work.model.WorkProjectResponseDto;
import com.project.work.model.WorkProjectUpdateDto;
import com.project.work.model.entity.WorkProject;
import com.project.work.model.entity.WorkProjectSetting;
import com.project.work.service.WorkProjectService;
import jakarta.validation.Valid;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
public class WorkProjectApi {
    private final static String VCS_ROUTING_KEY = "vcs-integration";
    private final static String KUBE_ROUTING_KEY = "kubernetes";

    private final WorkProjectService workProjectService;
    private final ProducerService producerService;

    public WorkProjectApi(WorkProjectService workProjectService, ProducerService producerService) {
        this.workProjectService = workProjectService;
        this.producerService = producerService;
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<WorkProjectRequestDto>> createWorkProject(
            final @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            final @Valid @RequestBody WorkProjectRequestDto dto) {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setHeader(HttpHeaders.AUTHORIZATION, authorizationHeader);
        try {
            producerService.sendMessageQueue(VCS_ROUTING_KEY, dto, messageProperties);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        WorkProject createdWorkProject = new WorkProject(dto.getId(), dto.getVendor());
        WorkProjectSetting createdSetting = new WorkProjectSetting(createdWorkProject.getId(), dto.getBuildType(), dto.getJavaVersion(), dto.getPort(), dto.getWorkDirectory(), dto.getEnvVariables(), dto.getArgs());
        workProjectService.createWorkProject(createdWorkProject, createdSetting);
        return ApiResponse.success(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<WorkProjectResponseDto>> getWorkProjectConfigById(
            final @PathVariable("id") String projectId) {
        WorkProject workProject = workProjectService.getWorkProjectById(projectId);
        WorkProjectSetting setting = workProjectService.getSettingByWorkProjectId(workProject.getId());

        WorkProjectResponseDto response = new WorkProjectResponseDto(workProject.getId(), workProject.getVendor(), setting.getJavaVersion(), setting.getBuildType(), setting.getWorkDirectory(), setting.getPort(), setting.getEnvVariables(), setting.getExecuteParameters());
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
            final @RequestBody WorkProjectUpdateDto dto) {
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

    @PatchMapping("/deploy/{id}")
    public ResponseEntity<Void> deployRepositoryById(
            final @PathVariable("id") String projectId) {
        WorkProject clonedWorkProject = workProjectService.getWorkProjectById(projectId);

        try {
            producerService.sendMessageQueue(KUBE_ROUTING_KEY, clonedWorkProject);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
