package com.project.workaholic.vcs.service;

import com.project.workaholic.vcs.model.OAuthGithubAccessTokenRequestDto;
import com.project.workaholic.vcs.model.OAuthGithubAccessTokenResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OAuthGithubService {
    private final WebClient webClient;
    private final String GITHUB_ACCESS_TOKEN_URL = "https://github.com/login/oauth/access_token";
    @Value("${github.client-id}")
    private String clientId;
    @Value("${github.client-secret}")
    private String clientSecret;

    public String getAccessToken(String code) {
        OAuthGithubAccessTokenRequestDto requestBody = OAuthGithubAccessTokenRequestDto.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .code(code)
                .build();

        String response =
                webClient.post()
                        .uri(GITHUB_ACCESS_TOKEN_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .bodyValue(
                                requestBody
                        )
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();

        return response;
    }

    public void getUserInfo(String accessToken, String uri) {
        String userProfile = Objects.requireNonNull(
                webClient.get()
                        .uri(uri)
                        .header(HttpHeaders.AUTHORIZATION, "token " + accessToken)
                        .retrieve()
                        .toEntity(String.class)
                        .block())
                .getBody();

        return;
    }
}
