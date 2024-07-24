package com.project.workaholic.deploy.api;

import com.project.workaholic.deploy.service.DeployService;
import com.project.workaholic.config.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @Operation(summary = "Namespace 속해있는 전체 Pod 조회", description = "Namespace 환경에 존재하는 전체 Pod 조회 API")
    @GetMapping("/pod/{namespace}")
    public ResponseEntity<ApiResponse<List<String>>> getPods(
            final @PathVariable String namespace) {
        List<String> pods = deployService.getPodByNamespace(namespace);
        return ApiResponse.success(pods);
    }

    @Operation(summary = "Pod 삭제", description = "Container 환경에 선택된 Pod 삭제 API")
    @DeleteMapping("/pod/{namespace}")
    public ResponseEntity<Void> deleteDeployment(
            final @PathVariable String namespace,
            final @RequestParam("id") String podName) {
        deployService.removePod(namespace, podName);
        return ApiResponse.success();
    }
}
