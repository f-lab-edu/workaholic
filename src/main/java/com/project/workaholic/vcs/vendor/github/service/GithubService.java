package com.project.workaholic.vcs.vendor.github.service;

import com.project.workaholic.vcs.vendor.github.model.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@ConfigurationProperties(prefix = "oauth2.github")
public class GithubService {
    private final String BASE_URL;
    private final String ACCEPT;
    private final String ACCESS_TOKEN_URL;
    private final String CLIENT_ID;
    private final String CLIENT_SECRET;
    private final RestTemplate restTemplate;

    public GithubService(String BASE_URL, String ACCEPT, String ACCESS_TOKEN_URL, String CLIENT_ID, String CLIENT_SECRET) {
        this.BASE_URL = BASE_URL;
        this.ACCEPT = ACCEPT;
        this.ACCESS_TOKEN_URL = ACCESS_TOKEN_URL;
        this.CLIENT_ID = CLIENT_ID;
        this.CLIENT_SECRET = CLIENT_SECRET;
        this.restTemplate = new RestTemplate();
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, ACCEPT);
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
        headers.add("X-GitHub-Api-Version", "2022-11-28");

        return headers;
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
    public List<GithubRepository> getRepositories(String accessToken) {
        HttpHeaders headers = getHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<GithubRepository[]> response = restTemplate.exchange(BASE_URL+"/user/repos", HttpMethod.GET, entity, GithubRepository[].class);
        return response.getBody() == null ? List.of() :  List.of(response.getBody());
    }

    //https://docs.github.com/ko/rest/branches/branches?apiVersion=2022-11-28#list-branches
    public List<GithubBranch> getBranches(String accessToken, String owner, String repo) {
        String url = String.format("%s/repos/%s/%s/branches", BASE_URL, owner, repo);
        HttpHeaders headers = getHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<GithubBranch[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, GithubBranch[].class);
        return response.getBody() == null ? List.of() :  List.of(response.getBody());
    }
}
