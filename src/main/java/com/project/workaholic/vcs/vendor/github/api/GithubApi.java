package com.project.workaholic.vcs.vendor.github.api;

import com.project.workaholic.config.interceptor.JsonWebTokenProvider;
import com.project.workaholic.response.model.ApiResponse;
import com.project.workaholic.response.model.enumeration.StatusCode;
import com.project.workaholic.vcs.service.OAuthService;
import com.project.workaholic.vcs.vendor.github.model.GithubRepository;
import com.project.workaholic.vcs.vendor.github.service.GithubService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/github")
public class GithubApi {
    private final GithubService githubService;
    private final JsonWebTokenProvider jsonWebTokenProvider;
    private final OAuthService oAuthService;

    @Operation(summary = "", description = "")
    @GetMapping("/repo")
    public ResponseEntity<ApiResponse<List<String>>> getRepositoriesFromVersionControlSystem(
            final HttpServletRequest request) {
        String accessToken = jsonWebTokenProvider.extractAccessToken(request);
        String accountId = jsonWebTokenProvider.parseClaims(accessToken).getSubject();
        String oAuthAccessToken = oAuthService.getAccessToken(accountId);

        Flux<List<GithubRepository>> repositories = githubService.getRepositories(oAuthAccessToken)
                .flatMap(githubRepository -> {
                    return Flux.just(List.of(githubRepository));
                })
                .onErrorResume( error -> {
                    return Flux.just(List.of());
                });

        return ApiResponse.success(StatusCode.SUCCESS_READ_REPO_LIST, List.of());
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
