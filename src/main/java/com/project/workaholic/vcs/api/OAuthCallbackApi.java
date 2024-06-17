package com.project.workaholic.vcs.api;

import com.project.workaholic.config.interceptor.JsonWebToken;
import com.project.workaholic.config.interceptor.JsonWebTokenProvider;
import com.project.workaholic.response.model.ApiResponse;
import com.project.workaholic.response.model.enumeration.StatusCode;
import com.project.workaholic.vcs.model.entity.OAuthAccessToken;
import com.project.workaholic.vcs.vendor.github.model.GithubAccessTokenResponseDto;
import com.project.workaholic.vcs.model.enumeration.VCSVendor;
import com.project.workaholic.vcs.vendor.github.service.GithubService;
import com.project.workaholic.vcs.service.OAuthService;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Tag(name = "Call Back API", description = "OAuth CallBack API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/callback")
public class OAuthCallbackApi {
    private final OAuthService oAuthService;
    private final GithubService githubService;
    private final JsonWebTokenProvider jsonWebTokenProvider;

    @Operation(
            summary = "Github OAuth CallBack API",
            description = "Github 에 설정한 CallBack URL로 명명해 인증 서버를 통해서 받아온 code를 사용해 AccessToken 반환")
    @GetMapping("/github")
    private Mono<ResponseEntity<ApiResponse<StatusCode>>> githubOAuthCallback(
            final HttpServletRequest request,
            final @RequestParam String code) {
        return githubService.getAccessToken(code)
                .doOnNext(githubAccessTokenResponseDto -> {
                    String accessToken = jsonWebTokenProvider.extractAccessToken(request);
                    String accountId = jsonWebTokenProvider.parseClaims(accessToken).getSubject();
                    oAuthService.registerToken(accountId, githubAccessTokenResponseDto.getAccessToken(), VCSVendor.GITHUB);
                })
                .map(
                        mono -> ApiResponse.success(StatusCode.SUCCESS_AUTH_VCS)
                );
    }
}