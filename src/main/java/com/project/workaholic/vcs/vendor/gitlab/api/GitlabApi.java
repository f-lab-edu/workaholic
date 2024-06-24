package com.project.workaholic.vcs.vendor.gitlab.api;

import com.project.workaholic.response.model.ApiResponse;
import com.project.workaholic.response.model.enumeration.StatusCode;
import com.project.workaholic.vcs.model.entity.OAuthAccessToken;
import com.project.workaholic.vcs.model.enumeration.VCSVendor;
import com.project.workaholic.vcs.service.OAuthService;
import com.project.workaholic.vcs.vendor.gitlab.model.GitlabBranch;
import com.project.workaholic.vcs.vendor.gitlab.model.GitlabRepository;
import com.project.workaholic.vcs.vendor.gitlab.model.GitlabTokenResponse;
import com.project.workaholic.vcs.vendor.gitlab.service.GitlabService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gitlab")
public class GitlabApi {
    private final GitlabService gitlabService;
    private final OAuthService oAuthService;

    @Operation(
            summary = "Gitlab OAuth CallBack API",
            description = "Gitlab 에 설정한 CallBack URL로 명명해 인증 서버를 통해서 받아온 code를 사용해 AccessToken 반환")
    @GetMapping("/callback")
    private ResponseEntity<ApiResponse<StatusCode>> githubOAuthCallback(
            final @RequestParam String code,
            final @RequestParam String id) {
        GitlabTokenResponse gitlabToken = gitlabService.getAccessToken(code);
        if (gitlabToken == null)
            return ApiResponse.error(StatusCode.ERROR);
        oAuthService.registerToken(id, gitlabToken.getAccessToken(), VCSVendor.GITLAB);
        return ApiResponse.success(StatusCode.SUCCESS_AUTH_VCS);
    }

    @Operation(
            summary = "Gitlab get repository list API",
            description = "발급받은 OAuth AccessToken 을 이용해 Gitlab 에서 제공하는 레포지토리 목록을 가져오는 API 호출")
    @GetMapping("/repo")
    public ResponseEntity<ApiResponse<GitlabRepository>> getRepositoriesFromVersionControlSystem(
            final @RequestParam String id) {
        OAuthAccessToken oAuthAccessToken = oAuthService.findAccessTokenByAccountId(id, VCSVendor.GITLAB);
        GitlabRepository repositories = gitlabService.getRepositories(oAuthAccessToken.getToken());
        return ApiResponse.success(StatusCode.SUCCESS_READ_REPO_LIST, repositories);
    }

    @Operation(
            summary = "Gitlab get branch list API",
            description = "발급받은 OAuth AccessToken 을 이용해 Gitlab 에서 제공하는 레포지토리의 브랜치 목록을 가져오는 API 호출")
    @GetMapping("/branch")
    public ResponseEntity<ApiResponse<GitlabBranch>> getBranchesFromRepository(
            final @RequestParam String id,
            final @RequestParam String repo) {
        OAuthAccessToken oAuthAccessToken = oAuthService.findAccessTokenByAccountId(id, VCSVendor.GITLAB);
        GitlabBranch branches = gitlabService.getBranches(oAuthAccessToken.getToken(), repo);
        return ApiResponse.success(StatusCode.SUCCESS_READ_BRANCHES, branches);
    }
}
