package com.project.workaholic.vcs.vendor.gitlab.api;

import com.project.workaholic.config.exception.type.FailedOAuthToken;
import com.project.workaholic.config.exception.type.NotFoundOAuthTokenException;
import com.project.workaholic.response.model.ApiResponse;
import com.project.workaholic.response.model.enumeration.StatusCode;
import com.project.workaholic.vcs.model.entity.OAuthAccessToken;
import com.project.workaholic.vcs.model.enumeration.VCSVendor;
import com.project.workaholic.vcs.service.VCSApiService;
import com.project.workaholic.vcs.vendor.gitlab.model.GitlabBranch;
import com.project.workaholic.vcs.vendor.gitlab.model.GitlabTokenResponse;
import com.project.workaholic.vcs.vendor.gitlab.service.GitlabService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gitlab")
public class GitlabApi {
    private final GitlabService gitlabService;
    private final VCSApiService VCSApiService;

    private String getAccountIdByRequest(HttpServletRequest request) {
        return (String) request.getAttribute("id");
    }

    @Operation(
            summary = "Gitlab OAuth CallBack API",
            description = "Gitlab 에 설정한 CallBack URL로 명명해 인증 서버를 통해서 받아온 code를 사용해 AccessToken 반환")
    @GetMapping("/callback")
    private ResponseEntity<ApiResponse<StatusCode>> githubOAuthCallback(
            final @RequestParam String code,
            final @RequestParam String state) {
        GitlabTokenResponse gitlabToken = gitlabService.getAccessToken(code);
        if (gitlabToken == null)
            throw new FailedOAuthToken(VCSVendor.GITLAB);

        VCSApiService.registerToken(state, gitlabToken.getAccessToken(), VCSVendor.GITLAB);
        return ApiResponse.success(StatusCode.SUCCESS_AUTH_VCS);
    }

    @Operation(
            summary = "Gitlab get repository list API",
            description = "발급받은 OAuth AccessToken 을 이용해 Gitlab 에서 제공하는 레포지토리 목록을 가져오는 API 호출")
    @GetMapping("/repo")
    public ResponseEntity<ApiResponse<List<String>>> getRepositoriesFromVersionControlSystem(
            HttpServletRequest request) {
        String accountId = getAccountIdByRequest(request);
        OAuthAccessToken oAuthAccessToken = gitlabService.getOAuthAccessTokenByAccountId(accountId);
        if(oAuthAccessToken == null)
            throw new NotFoundOAuthTokenException(VCSVendor.GITLAB);
        List<String> repositories = gitlabService.getRepositoryNames(oAuthAccessToken.getToken());
        return ApiResponse.success(StatusCode.SUCCESS_READ_REPO_LIST, repositories);
    }

    @Operation(
            summary = "Gitlab get branch list API",
            description = "발급받은 OAuth AccessToken 을 이용해 Gitlab 에서 제공하는 레포지토리의 브랜치 목록을 가져오는 API 호출")
    @GetMapping("/branch")
    public ResponseEntity<ApiResponse<List<GitlabBranch>>> getBranchesFromRepository(
            HttpServletRequest request,
            final @RequestParam String repoId) {
        String accountId = getAccountIdByRequest(request);
        OAuthAccessToken oAuthAccessToken = gitlabService.getOAuthAccessTokenByAccountId(accountId);
        if(oAuthAccessToken == null)
            throw new NotFoundOAuthTokenException(VCSVendor.GITLAB);
        //TODO 생성한 프로젝트의 REPO ID 추가 필요
        List<GitlabBranch> branches = gitlabService.getBranches(oAuthAccessToken.getToken(), repoId);
        return ApiResponse.success(StatusCode.SUCCESS_READ_BRANCHES, branches);
    }
}
