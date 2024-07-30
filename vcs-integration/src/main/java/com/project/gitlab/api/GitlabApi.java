package com.project.gitlab.api;

import com.project.exception.type.FailedOAuthToken;
import com.project.gitlab.model.GitlabBranch;
import com.project.gitlab.model.GitlabTokenResponse;
import com.project.gitlab.service.GitlabService;
import com.project.oauth.model.entity.OAuthAccessToken;
import com.project.oauth.model.enumeration.VCSVendor;
import com.project.oauth.service.OAuthApiService;
import com.project.exception.type.NotFoundOAuthTokenException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    private final OAuthApiService oAuthApiService;

    private String getAccountIdByRequest(HttpServletRequest request) {
        return (String) request.getAttribute("id");
    }

    @GetMapping("/callback")
    private ResponseEntity<Void> githubOAuthCallback(
            final @RequestParam String code,
            final @RequestParam String state) {
        GitlabTokenResponse gitlabToken = gitlabService.getAccessToken(code);
        if (gitlabToken == null)
            throw new FailedOAuthToken(VCSVendor.GITLAB);

        oAuthApiService.registerToken(state, gitlabToken.getAccessToken(), VCSVendor.GITLAB);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/repo")
    public ResponseEntity<List<String>> getRepositoriesFromVersionControlSystem(
            HttpServletRequest request) {
        String accountId = getAccountIdByRequest(request);
        OAuthAccessToken oAuthAccessToken = gitlabService.getOAuthAccessTokenByAccountId(accountId);
        if(oAuthAccessToken == null)
            throw new NotFoundOAuthTokenException(VCSVendor.GITLAB);
        List<String> repositories = gitlabService.getRepositoryNames(oAuthAccessToken.getToken());
        return ResponseEntity.status(HttpStatus.OK).body(repositories);
    }

    @GetMapping("/branch")
    public ResponseEntity<List<GitlabBranch>> getBranchesFromRepository(
            HttpServletRequest request,
            final @RequestParam String repoId) {
        String accountId = getAccountIdByRequest(request);
        OAuthAccessToken oAuthAccessToken = gitlabService.getOAuthAccessTokenByAccountId(accountId);
        if(oAuthAccessToken == null)
            throw new NotFoundOAuthTokenException(VCSVendor.GITLAB);
        //TODO 생성한 프로젝트의 REPO ID 추가 필요
        List<GitlabBranch> branches = gitlabService.getBranches(oAuthAccessToken.getToken(), repoId);
        return ResponseEntity.status(HttpStatus.OK).body(branches);
    }
}
