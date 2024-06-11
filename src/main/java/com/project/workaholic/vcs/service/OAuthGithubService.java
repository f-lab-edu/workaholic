package com.project.workaholic.vcs.service;

import com.project.workaholic.vcs.model.GitHubUserInfo;
import com.project.workaholic.vcs.model.OAuthGithubAccessTokenRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OAuthGithubService {
    private final WebClient webClient;
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

    public String getAccessToken(String code) {
        OAuthGithubAccessTokenRequestDto requestBody = OAuthGithubAccessTokenRequestDto.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .code(code)
                .build();

        return webClient.post()
                        .uri(GITHUB_ACCESS_TOKEN_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .bodyValue(
                                requestBody
                        )
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();
    }

    public GitHubUserInfo getUserInfo(String accessToken) {
        return webClient.get()
                        .uri("https://api.github.com/user")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .retrieve()
                        .bodyToMono(GitHubUserInfo.class)
                        .block();
    }
}
