package com.project.work.api;

import com.project.config.response.ApiResponse;
import com.project.work.model.WorkProjectUpdateConfigReq;
import com.project.work.model.entity.WorkProject;
import com.project.work.model.entity.WorkProjectSetting;
import com.project.work.model.enumeration.VCSVendor;
import com.project.work.service.WorkProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/project")
public class WorkProjectApi {
    private final WorkProjectService workProjectService;

    public WorkProjectApi(WorkProjectService workProjectService) {
        this.workProjectService = workProjectService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Void> getWorkProjectConfigById(
            final @PathVariable("id") UUID projectId) {
        WorkProject workProject = workProjectService.getWorkProjectById(projectId);
        WorkProjectSetting setting = workProjectService.getSettingByWorkProjectId(workProject.getId());
        return ApiResponse.success();
    }

    @GetMapping("")
    public ResponseEntity<List<UUID>> getWorkProjectConfig() {
        List<UUID> response = workProjectService.getAllWorkProject()
                .stream().map(WorkProject::getId).toList();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<UUID>> createWorkProject(
            final String name,
            final VCSVendor vendor
    ) {
        // 가져온 Repository 정보를 기반으로 WorkProject 와 WorkProjectSetting Entity 구성
        WorkProject createdWorkProject = new WorkProject(name, vendor);
        WorkProjectSetting createdSetting = new WorkProjectSetting(createdWorkProject.getId());
        createdWorkProject = workProjectService.createWorkProject(createdWorkProject, createdSetting);

        return ApiResponse.success(createdWorkProject.getId());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UUID>> updateWorkProjectConfigById(
            final @PathVariable("id") UUID projectId,
            final @RequestBody WorkProjectUpdateConfigReq dto) {
        WorkProject existingWorkProject = workProjectService.getWorkProjectById(projectId);
//        updatedWorkProject = workProjectService.updateWorkProject(existingWorkProject, updatedWorkProject);

        return ApiResponse.success(existingWorkProject.getId());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkProjectById(
            final @PathVariable("id") UUID projectId) {
        WorkProject deletedWorkProject = workProjectService.getWorkProjectById(projectId);
        workProjectService.deleteWorkProject(deletedWorkProject);

        return ApiResponse.success();
    }
}
