package com.project.workaholic.vcs.api;

import com.project.workaholic.response.model.ApiResponse;
import com.project.workaholic.response.model.enumeration.StatusCode;
import com.project.workaholic.vcs.model.OAuthGithubAccessTokenRequestDto;
import com.project.workaholic.vcs.model.OAuthGithubAccessTokenResponseDto;
import com.project.workaholic.vcs.service.OAuthGithubService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Tag(name ="", description = "")
@RestController
@RequiredArgsConstructor
@RequestMapping("/vcs")
public class VersionControlSystemApi  {
    private final OAuthGithubService githubService;

    @Operation(summary = "Version Control System Auth", description = "VCS Github Auth 로그인 시도")
    @GetMapping("/oauth2/github")
    public ResponseEntity<ApiResponse<String>> getOAuth2AtGithub(@RequestParam("code") final String code) {
        final String response = githubService.getAccessToken(code);
        return ApiResponse.success(StatusCode.SUCCESS_AUTH_VCS, response);
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
