package com.project.workaholic.vcs.vendor.github.service;

import com.project.workaholic.vcs.vendor.github.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@ConfigurationProperties(prefix = "oauth2.github")
@RequiredArgsConstructor
public class GithubService {
    private final String LOGIN_URL;
    private final String ACCESS_TOKEN_URL;
    private final String CLIENT_ID;
    private final String CLIENT_SECRET;
    private final String REDIRECT_URL;

    private final WebClient webClient;

    public GithubService(String BASE_URL, String ACCEPT, String LOGIN_URL, String ACCESS_TOKEN_URL, String CLIENT_ID, String CLIENT_SECRET, String REDIRECT_URL, WebClient.Builder webClient) {
        this.LOGIN_URL = LOGIN_URL;
        this.ACCESS_TOKEN_URL = ACCESS_TOKEN_URL;
        this.CLIENT_ID = CLIENT_ID;
        this.CLIENT_SECRET = CLIENT_SECRET;
        this.REDIRECT_URL = REDIRECT_URL;
        this.webClient = webClient.clone()
                .baseUrl(BASE_URL)
                .defaultHeader(HttpHeaders.ACCEPT, ACCEPT)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .defaultHeader("X-GitHub-Api-Version", "2022-11-28")
                .build();
    }

    public RedirectView requestCode(RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("client_id", CLIENT_ID)
                .addAttribute("redirect_url", REDIRECT_URL)
                .addAttribute("state", UUID.randomUUID().toString());

        return new RedirectView(LOGIN_URL);
    }

    public Mono<GithubAccessTokenResponseDto> getAccessToken(String code) {
        GithubAccessTokenRequestDto requestBody = GithubAccessTokenRequestDto.builder()
                .clientId(CLIENT_ID)
                .clientSecret(CLIENT_SECRET)
                .code(code)
                .build();
        WebClient webClient = WebClient.builder().build();
        return webClient
                .post()
                .uri(ACCESS_TOKEN_URL)
                .bodyValue(
                        requestBody
                )
                .retrieve()
                .bodyToMono(GithubAccessTokenResponseDto.class);
    }

    //https://docs.github.com/ko/rest/users/users?apiVersion=2022-11-28#get-the-authenticated-user--fine-grained-access-tokens
    public Mono<GitHubUser> getUserInfo(String accessToken) {
        return webClient
                .get()
                .uri("/user")
                .headers(headers  -> headers.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(GitHubUser.class);
    }

    //https://docs.github.com/ko/rest/repos/repos?apiVersion=2022-11-28#list-repositories-for-the-authenticated-user
    public Flux<GithubRepository> getRepositories(String accessToken) {
        return webClient.get()
                .uri("/user/repos")
                .headers(header -> header.setBasicAuth(accessToken))
                .retrieve()
                .bodyToFlux(GithubRepository.class);
    }

    //https://docs.github.com/ko/rest/branches/branches?apiVersion=2022-11-28#list-branches
    public Flux<GithubBranch> getBranches(String accessToken, String owner, String repo) {
        return webClient.get()
                .uri("/repos/{owner}/{repo}/branches", owner, repo)
                .headers(header -> header.setBasicAuth(accessToken))
                .retrieve()
                .bodyToFlux(GithubBranch.class);
    }
}
