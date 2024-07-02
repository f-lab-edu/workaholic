package com.project.workaholic.vcs.vendor.github.api;

import com.project.workaholic.account.service.AccountService;
import com.project.workaholic.config.exception.CustomException;
import com.project.workaholic.response.model.ApiResponse;
import com.project.workaholic.response.model.enumeration.StatusCode;
import com.project.workaholic.vcs.model.entity.OAuthAccessToken;
import com.project.workaholic.vcs.model.enumeration.VCSVendor;
import com.project.workaholic.vcs.service.VCSApiService;
import com.project.workaholic.vcs.vendor.github.model.GithuTokenResponse;
import com.project.workaholic.vcs.vendor.github.model.GithubBranch;
import com.project.workaholic.vcs.vendor.github.service.GithubService;
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
@RequestMapping("/github")
public class GithubApi {
    private final GithubService githubService;
    private final VCSApiService VCSApiService;
    private final AccountService accountService;

    private String getAccountIdByRequest(HttpServletRequest request) {
        return (String) request.getAttribute("id");
    }

    @Operation(
            summary = "Github OAuth CallBack API",
            description = "Github 에 설정한 CallBack URL로 명명해 인증 서버를 통해서 받아온 code를 사용해 AccessToken 반환")
    @GetMapping("/callback")
    private ResponseEntity<ApiResponse<StatusCode>> githubOAuthCallback(
            final @RequestParam String code,
            final @RequestParam String state) {
        GithuTokenResponse githubToken = githubService.getAccessToken(code);
        if( githubToken == null )
            return ApiResponse.error(StatusCode.ERROR);

        OAuthAccessToken existingToken = githubService.getOAuthAccessTokenByAccountId(state);
        if( existingToken != null )
            VCSApiService.renewAccessToken(existingToken, githubToken.getAccessToken());
        VCSApiService.registerToken(state, githubToken.getAccessToken(), VCSVendor.GITHUB);
        return ApiResponse.success(StatusCode.SUCCESS_AUTH_VCS);
    }

    @Operation(
            summary = "Github get repository list API",
            description = "발급받은 OAuth AccessToken 을 이용해 Github 에서 제공하는 레포지토리 목록을 가져오는 API 호출")
    @GetMapping("/repo")
    public ResponseEntity<ApiResponse<List<String>>> getRepositoriesFromVersionControlSystem(
            HttpServletRequest request) {
        String accountId = getAccountIdByRequest(request);
        OAuthAccessToken oAuthAccessToken = githubService.getOAuthAccessTokenByAccountId(accountId);
        if (oAuthAccessToken == null )
            throw new CustomException(StatusCode.INVALID_ACCOUNT);
        List<String> repositories = githubService.getRepositoryNames(oAuthAccessToken.getToken());
        return ApiResponse.success(StatusCode.SUCCESS_READ_REPO_LIST, repositories);
    }

    @Operation(
            summary = "Github get branch list API",
            description = "발급받은 OAuth AccessToken 을 이용해 Github 에서 제공하는 레포지토리의 브랜치 목록을 가져오는 API 호출")
    @GetMapping("/branch")
    public ResponseEntity<ApiResponse<List<GithubBranch>>> getBranchesFromRepository(
            HttpServletRequest request,
            final @RequestParam String owner,
            final @RequestParam String repo) {
        String accountId = getAccountIdByRequest(request);
        OAuthAccessToken oAuthAccessToken = githubService.getOAuthAccessTokenByAccountId(accountId);
        if(oAuthAccessToken == null)
            throw new CustomException(StatusCode.INVALID_ACCOUNT);

        List<GithubBranch> branches = githubService.getBranches(oAuthAccessToken.getToken(), owner, repo);
        return ApiResponse.success(StatusCode.SUCCESS_READ_BRANCHES, branches);
    }
}
