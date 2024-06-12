package com.project.workaholic.vcs.service;

import com.project.workaholic.vcs.model.GitHubUserInfo;
import com.project.workaholic.vcs.model.GithubAccessTokenRequestDto;
import com.project.workaholic.vcs.model.GithubAccessTokenResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OAuthGithubService {
    private final WebClient webClient;
    private final String GITHUB_BASE_URL = "https://api.github.com";
    @Value("${oauth2.user.github.login-url}")
    private String GITHUB_LOGIN_URL;
    @Value("${oauth2.user.github.token-uri}")
    private String GITHUB_ACCESS_TOKEN_URL;
    @Value("${oauth2.user.github.client-id}")
    private String clientId;
    @Value("${oauth2.user.github.client-secret}")
    private String clientSecret;
    @Value("${oauth2.user.github.redirect-uri}")
    private String GITHUB_REDIRECT_URI;

    public RedirectView requestCode(RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("client_id", clientId)
                .addAttribute("redirect_url", GITHUB_REDIRECT_URI)
                .addAttribute("state", UUID.randomUUID().toString());

        return new RedirectView(GITHUB_LOGIN_URL);
    }

    public GithubAccessTokenResponseDto getAccessToken(String code) {
        GithubAccessTokenRequestDto requestBody = GithubAccessTokenRequestDto.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .code(code)
                .build();

        return webClient
                .post()
                .uri(GITHUB_ACCESS_TOKEN_URL)
                .headers(headers  -> {
                    headers.set("Accept", "application/vnd.github+json");
                    headers.set("X-GitHub-Api-Version", "2022-11-28");
                })
                .bodyValue(
                        requestBody
                )
                .retrieve()
                .bodyToMono(GithubAccessTokenResponseDto.class)
                .block();
    }

    //https://docs.github.com/ko/rest/users/users?apiVersion=2022-11-28#get-the-authenticated-user--fine-grained-access-tokens
    public GitHubUserInfo getUserInfo(String accessToken) {
        return webClient
                .get()
                .uri(GITHUB_BASE_URL + "/user")
                .headers(headers  -> {
                    headers.set("Accept", "application/vnd.github+json");
                    headers.setBearerAuth(accessToken);
                    headers.set("X-GitHub-Api-Version", "2022-11-28");
                })
                .retrieve()
                .bodyToMono(GitHubUserInfo.class)
                .block();
    }


}
