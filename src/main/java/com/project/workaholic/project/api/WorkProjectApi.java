package com.project.workaholic.project.api;

import com.project.workaholic.account.model.entity.Account;
import com.project.workaholic.account.service.AccountService;
import com.project.workaholic.config.exception.CustomException;
import com.project.workaholic.project.model.WorkProjectConfigReqDto;
import com.project.workaholic.project.model.WorkProjectConfigResDto;
import com.project.workaholic.project.model.WorkProjectListViewDto;
import com.project.workaholic.project.model.WorkProjectUpdateConfigReq;
import com.project.workaholic.project.model.entity.WorkProject;
import com.project.workaholic.project.service.WorkProjectService;
import com.project.workaholic.response.model.ApiResponse;
import com.project.workaholic.response.model.enumeration.StatusCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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

@RestController
@RequestMapping("/project")
public class WorkProjectApi {
    private final WorkProjectService workProjectService;
    private final AccountService accountService;

    public WorkProjectApi(WorkProjectService workProjectService, AccountService accountService) {
        this.workProjectService = workProjectService;
        this.accountService = accountService;
    }

    private WorkProjectConfigResDto toConfigResDto(WorkProject workProject) {
        return new WorkProjectConfigResDto(workProject.getName(), workProject.getRepositoryName(), workProject.getRepository(), List.of(), "COMMIT");
    }

    private WorkProjectListViewDto toListViewDto(WorkProject workProject) {
        return WorkProjectListViewDto.builder()
                .name(workProject.getName())
                .repositoryName(workProject.getRepositoryName())
                .build();
    }

    private WorkProject toEntity(WorkProjectConfigReqDto dto, String accountId) {
        return new WorkProject(dto.getName(), dto.getRepository(), dto.getRepositoryName(), accountId, dto.getVendor());
    }

    @Operation(summary = "프로젝트 조회 API", description = "ID에 해당되는 프로젝트에 대한 자세한 정보를 조회하는 API", tags = "Project API")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<WorkProjectConfigResDto>> getWorkProjectConfigById(
            HttpServletRequest request,
            final @Parameter(description = "프로젝트 아이디") @PathVariable("id") String projectId) {
        WorkProject workProject = workProjectService.getWorkProjectById(projectId);
        return ApiResponse.success(StatusCode.SUCCESS_READ_PROJECT, toConfigResDto(workProject));
    }

    @Operation(summary = "프로젝트 목록 조회 API", description = "전체 프로젝트 목록을 조회하는 API")
    @GetMapping("")
    public ResponseEntity<ApiResponse<List<WorkProjectListViewDto>>> getWorkProjectConfig() {
        List<WorkProjectListViewDto> projectList =
                workProjectService.getAllWorkProjects().stream().map(this::toListViewDto).toList();

        return ApiResponse.success(StatusCode.SUCCESS_READ_PROJECT_LIST, projectList);
    }

    @Operation(summary = "프로젝트 생성 API", description = "Request Body 데이터를 통해서 새로운 프로젝트를 생성하는 API", tags = "Project API")
    @PostMapping("")
    public ResponseEntity<ApiResponse<String>> createWorkProject(
            final HttpServletRequest httpServletRequest,
            final @Valid @Parameter(description = "WorkProject config form") @RequestBody WorkProjectConfigReqDto dto) {
        String accountId = httpServletRequest.getHeader("id");
        if(accountId == null || !accountService.checkExistAccountById(accountId))
            throw new CustomException(StatusCode.INVALID_ACCOUNT);



        WorkProject createdWorkProject = toEntity(dto, accountId);
        createdWorkProject = workProjectService.createWorkProject(createdWorkProject);
        return ApiResponse.success(StatusCode.SUCCESS_CREATE_PROJECT, createdWorkProject.getId());
    }

    @Operation(summary = "프로젝트 수정 API", description = "ID에 해당된 프로젝트의 설정을 수정하는 API", tags = "Project API")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<WorkProjectConfigResDto>> updateWorkProjectConfigById(
            final @Parameter(description = "프로젝트 아이디") @PathVariable("id") String projectId,
            final @Valid @Parameter(description = "WorkProject config form") @RequestBody WorkProjectUpdateConfigReq dto) {
        WorkProject existingWorkProject = workProjectService.getWorkProjectById(projectId);
//        WorkProject updatedWorkProject = toEntity(dto);
//        updatedWorkProject = workProjectService.updateWorkProject(existingWorkProject, updatedWorkProject);

        return ApiResponse.success(StatusCode.SUCCESS_UPDATE_PROJECT, toConfigResDto(existingWorkProject));
    }

    @Operation(summary = "프로젝트 삭제 API", description = "ID에 해당되는 프로젝트 삭제 API", tags = "Project API")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<StatusCode>> deleteWorkProjectById(
            final @Parameter(description = "프로젝트 아이디") @PathVariable("id") String projectId) {
        WorkProject deletedWorkProject = workProjectService.getWorkProjectById(projectId);
        workProjectService.deleteWorkProject(deletedWorkProject);

        return ApiResponse.success(StatusCode.SUCCESS_DELETE_PROJECT);
    }
}