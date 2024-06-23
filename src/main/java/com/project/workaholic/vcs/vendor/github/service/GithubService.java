package com.project.workaholic.vcs.vendor.github.service;

import com.project.workaholic.vcs.vendor.github.model.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.UUID;

@ConfigurationProperties(prefix = "oauth2.github")
public class GithubService {
    private final String BASE_URL;
    private final String ACCEPT;
    private final String LOGIN_URL;
    private final String ACCESS_TOKEN_URL;
    private final String CLIENT_ID;
    private final String CLIENT_SECRET;
    private final String REDIRECT_URL;
    private final RestTemplate restTemplate;

    public GithubService(String BASE_URL, String ACCEPT, String LOGIN_URL, String ACCESS_TOKEN_URL, String CLIENT_ID, String CLIENT_SECRET, String REDIRECT_URL) {
        this.BASE_URL = BASE_URL;
        this.ACCEPT = ACCEPT;
        this.LOGIN_URL = LOGIN_URL;
        this.ACCESS_TOKEN_URL = ACCESS_TOKEN_URL;
        this.CLIENT_ID = CLIENT_ID;
        this.CLIENT_SECRET = CLIENT_SECRET;
        this.REDIRECT_URL = REDIRECT_URL;
        this.restTemplate = new RestTemplate();
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, ACCEPT);
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
        headers.add("X-GitHub-Api-Version", "2022-11-28");

        return headers;
    }

    public RedirectView requestCode(RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("client_id", CLIENT_ID)
                .addAttribute("redirect_uri", REDIRECT_URL)
                .addAttribute("state", UUID.randomUUID().toString());

        return new RedirectView(LOGIN_URL);
    }

    public GithuTokenResponse getAccessToken(String code) {
        HttpHeaders headers = getHeaders();
        GithubTokenRequest body = GithubTokenRequest.builder()
                .clientId(CLIENT_ID)
                .clientSecret(CLIENT_SECRET)
                .code(code)
                .build();

        HttpEntity<GithubTokenRequest> entity = new HttpEntity<>(body, headers);
        return restTemplate.postForObject(ACCESS_TOKEN_URL, entity, GithuTokenResponse.class);
    }

    //https://docs.github.com/ko/rest/repos/repos?apiVersion=2022-11-28#list-repositories-for-the-authenticated-user
    public GithubRepository getRepositories(String accessToken) {
        HttpHeaders headers = getHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.getForObject(BASE_URL+"/user/repos", GithubRepository.class, entity);
    }

    //https://docs.github.com/ko/rest/branches/branches?apiVersion=2022-11-28#list-branches
    public GithubBranch getBranches(String accessToken, String owner, String repo) {
        String url = String.format("%s/repos/%s/%s/branches", BASE_URL, owner, repo);
        HttpHeaders headers = getHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.getForObject(url, GithubBranch.class, entity);
    }
}
