package com.project.workaholic.vcs.api;

import com.project.workaholic.response.model.ApiResponse;
import com.project.workaholic.response.model.enumeration.StatusCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name ="", description = "")
@RestController
@RequiredArgsConstructor
@RequestMapping("/vcs")
public class VersionControlSystemApi  {

    @Operation(summary = "Version Control System Auth", description = "vcs Auth 로그인")
    @PostMapping("/auth")
    public ResponseEntity<ApiResponse<StatusCode>> authVersionControlSystem(String repoName, String url) {
        return ApiResponse.success(StatusCode.SUCCESS_AUTH_VCS);
    }

    @Operation(summary = "", description = "")
    @GetMapping("/repo")
    public ResponseEntity<ApiResponse<List<String>>> getRepositoriesFromVersionControlSystem() {
        return ApiResponse.success(StatusCode.SUCCESS_IMPORT_REPO, List.of("feature"));
    }

    @Operation(summary = "", description = "")
    @GetMapping("/repo/{name}")
    public ResponseEntity<ApiResponse<StatusCode>> importRepositoryByName(
            @PathVariable("name") String repoName) {

        return ApiResponse.success(StatusCode.SUCCESS_IMPORT_REPO);
    }

    @Operation(summary = "", description = "")
    @GetMapping("/commit")
    public ResponseEntity<ApiResponse<StatusCode>> getCommitsFromRepository() {
        return ApiResponse.success(StatusCode.SUCCESS_READ_COMMIT_LIST);
    }

    @Operation(summary = "", description = "")
    @GetMapping("/branch")
    public ResponseEntity<ApiResponse<StatusCode>> getBranchesFromRepository() {
        return ApiResponse.success(StatusCode.SUCCESS_READ_BRANCHES);
    }

}
