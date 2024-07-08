package com.project.workaholic.deploy.api;

import com.project.workaholic.deploy.model.PodDto;
import com.project.workaholic.deploy.model.PodInfoDto;
import com.project.workaholic.deploy.service.DeployService;
import com.project.workaholic.response.model.ApiResponse;
import com.project.workaholic.response.model.enumeration.StatusCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Deploy API", description = "Workaholic Container 환경에 대한 배포관련 API")
@RestController
@RequestMapping("/container")
public class DeployApi {
    private final DeployService deployService;

    public DeployApi(DeployService deployService) {
        this.deployService = deployService;
    }

    @Operation(summary = "전체 Pod 조회", description = "Container 환경에 존재하는 전체 Pod 조회 API")
    @GetMapping("/pod")
    public ResponseEntity<ApiResponse<List<PodDto>>> getPods() {
        List<PodDto> pods = deployService.getPods();
        return ApiResponse.success(StatusCode.SUCCESS_READ_PODS, pods);
    }

    @Operation(summary = "Pod 조회", description = "Container 환경에 선택된 Pod 조회 API")
    @GetMapping("/pod/{id}")
    public ResponseEntity<ApiResponse<PodInfoDto>> getPodById(
            final @Parameter(name = "아이디", description = "Pod 아이디")
            @PathVariable("id") String podId) {
        return ApiResponse.success(StatusCode.SUCCESS_READ_POD, PodInfoDto.builder().build());
    }

    @Operation(summary = "Pod 삭제", description = "Container 환경에 선택된 Pod 삭제 API")
    @DeleteMapping("/pod/{id}")
    public ResponseEntity<ApiResponse<StatusCode>> deletePodById(
            final @Parameter(name = "아이디", description = "Pod 아이디")
            @PathVariable("id") String podId) {
        return ApiResponse.success(StatusCode.SUCCESS_DELETE_POD);
    }

    @Operation(summary = "Pod 수정", description = "Container 환경에 선택된 Pod 수정 API")
    @PutMapping("/pod/{id}")
    public ResponseEntity<ApiResponse<String>> updatePodById(
            final @Parameter(name = "아이디", description = "Pod 아이디")
            @PathVariable("id") String podId,
            @Valid @RequestBody PodDto configDto) {
        return ApiResponse.success(StatusCode.SUCCESS_UPDATE_POD, podId);
    }

    @Operation(summary = "프로젝트 배포", description = "Container 환경에 존재하는 Pod 배포하는 API")
    @PostMapping("/pod/{id}/deploy")
    public ResponseEntity<ApiResponse<StatusCode>> deployment(
            final @Parameter(name = "아이디", description = "Pod 아이디")
            @PathVariable("id") String podId) {
        return ApiResponse.success(StatusCode.SUCCESS_DEPLOY_POD);
    }
}
