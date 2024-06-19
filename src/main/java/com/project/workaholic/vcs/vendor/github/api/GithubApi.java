package com.project.workaholic.vcs.vendor.github.api;

import com.project.workaholic.config.interceptor.JsonWebTokenProvider;
import com.project.workaholic.response.model.ApiResponse;
import com.project.workaholic.response.model.enumeration.StatusCode;
import com.project.workaholic.vcs.model.entity.OAuthAccessToken;
import com.project.workaholic.vcs.model.enumeration.VCSVendor;
import com.project.workaholic.vcs.service.OAuthService;
import com.project.workaholic.vcs.vendor.github.model.GithuTokenResponse;
import com.project.workaholic.vcs.vendor.github.model.GithubRepository;
import com.project.workaholic.vcs.vendor.github.service.GithubService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/github")
public class GithubApi {
    private final GithubService githubService;
    private final JsonWebTokenProvider jsonWebTokenProvider;
    private final OAuthService oAuthService;

    @Operation(
            summary = "Github OAuth CallBack API",
            description = "Github 에 설정한 CallBack URL로 명명해 인증 서버를 통해서 받아온 code를 사용해 AccessToken 반환")
    @GetMapping("/callback")
    private ResponseEntity<ApiResponse<StatusCode>> githubOAuthCallback(
            final @RequestParam String code) {
//        String accountId = jsonWebTokenProvider.parseClaims(jwt).getSubject();
        GithuTokenResponse githubToken = githubService.getAccessToken(code);

//        if( githubToken == null )
//            return ApiResponse.error(StatusCode.ERROR);
        oAuthService.registerToken("test@example.com", githubToken.getAccessToken(), VCSVendor.GITHUB);
        return ApiResponse.success(StatusCode.SUCCESS_AUTH_VCS);
    }

    @Operation(summary = "", description = "")
    @GetMapping("/repo")
    public ResponseEntity<ApiResponse<GithubRepository>> getRepositoriesFromVersionControlSystem(
            final HttpServletRequest request) {
        String accessToken = jsonWebTokenProvider.extractAccessToken(request);
        String accountId = jsonWebTokenProvider.parseClaims(accessToken).getSubject();
        OAuthAccessToken oAuthAccessToken = oAuthService.findAccessTokenByAccountId(accountId);
        GithubRepository repositories = githubService.getRepositories(oAuthAccessToken.getToken());
        return ApiResponse.success(StatusCode.SUCCESS_READ_REPO_LIST, repositories);
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
