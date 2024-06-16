package com.project.workaholic.vcs.api;

import com.project.workaholic.response.model.ApiResponse;
import com.project.workaholic.response.model.enumeration.StatusCode;
import com.project.workaholic.vcs.model.GitHubUserInfo;
import com.project.workaholic.vcs.model.GithubAccessTokenResponseDto;
import com.project.workaholic.vcs.model.enumeration.VCSVendor;
import com.project.workaholic.vcs.service.OAuthGithubService;
import com.project.workaholic.vcs.service.OAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    private final OAuthService oAuthService;
    private final OAuthGithubService githubService;

    @Operation(
            summary = "Github OAuth CallBack API",
            description = "Github 에 설정한 CallBack URL로 명명해 인증 서버를 통해서 받아온 code를 사용해 AccessToken 반환")
    @GetMapping("/github")
    private ResponseEntity<ApiResponse<StatusCode>> githubOAuthCallback(
            final @RequestParam String code) {
        GithubAccessTokenResponseDto oAuthAccessToken = githubService.getAccessToken(code);
        oAuthService.registerToken("test", oAuthAccessToken.getAccessToken(), VCSVendor.GITHUB);
        GitHubUserInfo githubUserInfo = githubService.getUserInfo(oAuthAccessToken.getAccessToken());
        return ApiResponse.success(StatusCode.SUCCESS_IMPORT_REPO);
    }
}
