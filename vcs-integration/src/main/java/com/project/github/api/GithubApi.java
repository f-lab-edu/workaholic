package com.project.github.api;

import com.project.exception.type.FailedCloneRepositoryException;
import com.project.exception.type.FailedOAuthToken;
import com.project.exception.type.NotFoundOAuthTokenException;
import com.project.github.model.GithuTokenResponse;
import com.project.github.model.GithubBranch;
import com.project.github.service.GithubService;
import com.project.oauth.model.entity.OAuthAccessToken;
import com.project.oauth.model.enumeration.VCSVendor;
import com.project.oauth.service.OAuthApiService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/github")
public class GithubApi {
    private final GithubService githubService;
    private final OAuthApiService oAuthApiService;

    public GithubApi(GithubService githubService, OAuthApiService oAuthApiService) {
        this.githubService = githubService;
        this.oAuthApiService = oAuthApiService;
    }

    private String getAccountIdByRequest(HttpServletRequest request) {
        return (String) request.getAttribute("id");
    }

    @GetMapping("/callback")
    private ResponseEntity<Void> githubOAuthCallback(
            final @RequestParam String code,
            final @RequestParam String state) {
        GithuTokenResponse githubToken = githubService.getAccessToken(code);
        if( githubToken == null )
            throw new FailedOAuthToken(VCSVendor.GITHUB);

        OAuthAccessToken existingToken = githubService.getOAuthAccessTokenByAccountId(state);
        if( existingToken != null )
            oAuthApiService.renewAccessToken(existingToken, githubToken.getAccessToken());
        oAuthApiService.registerToken(state, githubToken.getAccessToken(), VCSVendor.GITHUB);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/repo")
    public ResponseEntity<List<String>> getRepositoriesFromVersionControlSystem(
            final HttpServletRequest request) {
        String accountId = getAccountIdByRequest(request);
        OAuthAccessToken oAuthAccessToken = githubService.getOAuthAccessTokenByAccountId(accountId);
        if (oAuthAccessToken == null )
            throw new NotFoundOAuthTokenException(VCSVendor.GITHUB);
        List<String> repositories = githubService.getRepositoryNames(oAuthAccessToken.getToken());
        return ResponseEntity.status(HttpStatus.OK).body(repositories);
    }

    @GetMapping("/branch")
    public ResponseEntity<List<GithubBranch>> getBranchesFromRepository(
            final HttpServletRequest request,
            final @RequestParam String owner,
            final @RequestParam String repo) {
        String accountId = getAccountIdByRequest(request);
        OAuthAccessToken oAuthAccessToken = githubService.getOAuthAccessTokenByAccountId(accountId);
        if(oAuthAccessToken == null)
            throw new NotFoundOAuthTokenException(VCSVendor.GITHUB);

        List<GithubBranch> branches = githubService.getBranches(oAuthAccessToken.getToken(), owner, repo);
        return ResponseEntity.status(HttpStatus.OK).body(branches);
    }

    @PatchMapping("/clone")
    public ResponseEntity<Void> cloningRepository(
            final String workId,
            final String githubId,
            final String cloneUrl
    ) {
        OAuthAccessToken oAuthAccessToken = githubService.getOAuthAccessTokenByAccountId(githubId);

        try {
            String clonedPath = githubService.cloneRepository(workId, cloneUrl, oAuthAccessToken.getToken());
        } catch (FailedCloneRepositoryException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
