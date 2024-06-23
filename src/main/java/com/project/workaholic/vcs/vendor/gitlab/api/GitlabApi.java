package com.project.workaholic.vcs.vendor.gitlab.api;

import com.project.workaholic.config.interceptor.JsonWebTokenProvider;
import com.project.workaholic.response.model.ApiResponse;
import com.project.workaholic.response.model.enumeration.StatusCode;
import com.project.workaholic.vcs.model.entity.OAuthAccessToken;
import com.project.workaholic.vcs.model.enumeration.VCSVendor;
import com.project.workaholic.vcs.service.OAuthService;
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
    private final JsonWebTokenProvider jsonWebTokenProvider;
    private final OAuthService oAuthService;

    @Operation(
            summary = "Github OAuth CallBack API",
            description = "Github 에 설정한 CallBack URL로 명명해 인증 서버를 통해서 받아온 code를 사용해 AccessToken 반환")
    @GetMapping("/callback")
    private ResponseEntity<ApiResponse<StatusCode>> githubOAuthCallback(
            final @RequestParam String jwt,
            final @RequestParam String code) {
        GitlabTokenResponse gitlabToken = gitlabService.getAccessToken(code);
        String accountId = jsonWebTokenProvider.parseClaims(jwt).getSubject();
        OAuthAccessToken existingToken = oAuthService.findAccessTokenByAccountId(accountId);

        if ( existingToken == null ) {
            oAuthService.registerToken("test@example.com", gitlabToken.getAccessToken(), VCSVendor.GITHUB);
            return ApiResponse.success(StatusCode.SUCCESS_AUTH_VCS);
        }
        oAuthService.updateAccessToken(existingToken, gitlabToken.getAccessToken());
        return ApiResponse.success(StatusCode.SUCCESS_AUTH_VCS);
    }
}
