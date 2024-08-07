package com.project.workaholic.project.api;

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
import com.project.workaholic.vcs.model.VCSRepository;
import com.project.workaholic.vcs.model.entity.OAuthAccessToken;
import com.project.workaholic.vcs.service.VCSApiService;
import com.project.workaholic.vcs.service.VendorApiService;
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
    private final VCSApiService vcsApiService;

    public WorkProjectApi(WorkProjectService workProjectService, AccountService accountService, VCSApiService vcsApiService) {
        this.workProjectService = workProjectService;
        this.accountService = accountService;
        this.vcsApiService = vcsApiService;
    }

    private WorkProjectConfigResDto toConfigResDto(WorkProject workProject, VCSRepository vcsRepository) {
        return new WorkProjectConfigResDto(workProject.getName(), workProject.getRepositoryName(), workProject.getRepositoryName(), List.of(), "COMMIT");
    }

    private WorkProjectConfigResDto toConfigResDto(WorkProject workProject) {
        return new WorkProjectConfigResDto(workProject.getName(), workProject.getRepositoryName(), workProject.getRepositoryName(), List.of(), "COMMIT");
    }

    private WorkProjectListViewDto toListViewDto(WorkProject workProject) {
        return new WorkProjectListViewDto(workProject.getId(), workProject.getName(), workProject.getVendor(), workProject.getRepositoryName(), "commit", "branchName");
    }

    private String getAccountIdFromRequest(HttpServletRequest request) {
        return (String) request.getAttribute("id");
    }

    @Operation(summary = "프로젝트 조회 API", description = "ID에 해당되는 프로젝트에 대한 자세한 정보를 조회하는 API", tags = "Project API")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<WorkProjectConfigResDto>> getWorkProjectConfigById(
            final HttpServletRequest request,
            final @Parameter(description = "프로젝트 아이디") @PathVariable("id") String projectId) {
        String accountId = getAccountIdFromRequest(request);
        if(accountId == null || !accountService.checkExistAccountById(accountId))
            throw new CustomException(StatusCode.INVALID_ACCOUNT);
        WorkProject workProject = workProjectService.getWorkProjectById(projectId);
        OAuthAccessToken oAuthAccessToken = vcsApiService.getOAuthAccessTokenByAccountId(workProject.getVendor(), accountId);
        VCSRepository importedRepository = vcsApiService.getSourceRepository(oAuthAccessToken.getToken(), workProject);

        return ApiResponse.success(StatusCode.SUCCESS_READ_PROJECT, toConfigResDto(workProject, importedRepository));
    }

    @Operation(summary = "프로젝트 목록 조회 API", description = "전체 프로젝트 목록을 조회하는 API", tags = "Project API")
    @GetMapping("")
    public ResponseEntity<ApiResponse<List<WorkProjectListViewDto>>> getWorkProjectConfig(
            final HttpServletRequest request) {
        String accountId = getAccountIdFromRequest(request);
        List<WorkProjectListViewDto> projectList =
                workProjectService.getAllWorkProjectsByAccountId(accountId).stream().map(this::toListViewDto).toList();

        return ApiResponse.success(StatusCode.SUCCESS_READ_PROJECT_LIST, projectList);
    }

    @Operation(summary = "프로젝트 생성 API", description = "Request Body 데이터를 통해서 새로운 프로젝트를 생성하는 API", tags = "Project API")
    @PostMapping("")
    public ResponseEntity<ApiResponse<String>> createWorkProject(
            final HttpServletRequest request,
            final @Valid @Parameter(description = "WorkProject config form") @RequestBody WorkProjectConfigReqDto dto) {
        String accountId = getAccountIdFromRequest(request);
        if(accountId == null || !accountService.checkExistAccountById(accountId))
            throw new CustomException(StatusCode.INVALID_ACCOUNT);

        OAuthAccessToken oAuthAccessToken = vcsApiService.getOAuthAccessTokenByAccountId(dto.getVendor(), accountId);
        VendorApiService service = vcsApiService.getMatchServiceByVendor(dto.getVendor());
        VCSRepository vcsRepository = service.getRepositoryInformation(oAuthAccessToken.getToken(), dto.getRepositoryName());

        WorkProject createdWorkProject = new WorkProject(dto.getName(), dto.getRepositoryName(), vcsRepository.getCommitsUrl(), vcsRepository.getBranchesUrl(), vcsRepository.getCloneUrl(), dto.getVendor(), accountId);
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
