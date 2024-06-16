package com.project.workaholic.vcs.service;

import com.project.workaholic.vcs.model.GitHubUserInfo;
import com.project.workaholic.vcs.model.GithubAccessTokenRequestDto;
import com.project.workaholic.vcs.model.GithubAccessTokenResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.UUID;

@ConfigurationProperties(prefix = "oauth2.github")
@RequiredArgsConstructor
public class OAuthGithubService {
    private final String BASE_URL;
    private final String LOGIN_URL;
    private final String ACCESS_TOKEN_URL;
    private final String CLIENT_ID;
    private final String CLIENT_SECRET;
    private final String REDIRECT_URL;

    public RedirectView requestCode(RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("client_id", CLIENT_ID)
                .addAttribute("redirect_url", REDIRECT_URL)
                .addAttribute("state", UUID.randomUUID().toString());

        return new RedirectView(LOGIN_URL);
    }

    public GithubAccessTokenResponseDto getAccessToken(String code) {
        GithubAccessTokenRequestDto requestBody = GithubAccessTokenRequestDto.builder()
                .clientId(CLIENT_ID)
                .clientSecret(CLIENT_SECRET)
                .code(code)
                .build();
        WebClient webClient = WebClient.builder().build();
        return webClient
                .post()
                .uri(ACCESS_TOKEN_URL)
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
        WebClient webClient = WebClient.builder().build();
        return webClient
                .get()
                .uri(BASE_URL + "/user")
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
