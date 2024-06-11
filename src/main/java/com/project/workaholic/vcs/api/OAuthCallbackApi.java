package com.project.workaholic.vcs.api;

import com.project.workaholic.response.model.ApiResponse;
import com.project.workaholic.response.model.enumeration.StatusCode;
import com.project.workaholic.vcs.model.GitHubUserInfo;
import com.project.workaholic.vcs.service.OAuthGithubService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Call Back API", description = "OAuth CallBack API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/callback")
public class OAuthCallbackApi {
    private final OAuthGithubService githubService;

    @Operation(summary = "", description = "")
    @GetMapping("/github")
    private ResponseEntity<ApiResponse<String>> githubOAuthCallback(
            @RequestParam String code, HttpServletRequest request) {
        String oAuthAccessToken = githubService.getAccessToken(code);
        GitHubUserInfo info = githubService.getUserInfo(oAuthAccessToken);
        return ApiResponse.success(StatusCode.SUCCESS_IMPORT_REPO, oAuthAccessToken);
    }
}
