package com.project.workaholic.vcs.vendor.gitlab.service;

import com.project.workaholic.vcs.vendor.gitlab.model.GitlabBranch;
import com.project.workaholic.vcs.vendor.gitlab.model.GitlabRepository;
import com.project.workaholic.vcs.vendor.gitlab.model.GitlabTokenRequest;
import com.project.workaholic.vcs.vendor.gitlab.model.GitlabTokenResponse;
import com.project.workaholic.vcs.vendor.gitlab.model.enumeration.GitlabGrantType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@ConfigurationProperties(prefix = "oauth2.gitlab")
public class GitlabService {
    private final String BASE_URL;
    private final String ACCESS_TOKEN_URL;
    private final String CLIENT_ID;
    private final String CLIENT_SECRET;
    private final String REDIRECT_URI;
    private final RestTemplate restTemplate;

    public GitlabService(String BASE_URL, String ACCESS_TOKEN_URL, String CLIENT_ID, String REDIRECT_URI, String CLIENT_SECRET) {
        this.BASE_URL = BASE_URL;
        this.ACCESS_TOKEN_URL = ACCESS_TOKEN_URL;
        this.CLIENT_ID = CLIENT_ID;
        this.CLIENT_SECRET = CLIENT_SECRET;
        this.REDIRECT_URI = REDIRECT_URI;
        this.restTemplate = new RestTemplate();
    }

    public GitlabTokenResponse getAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        GitlabTokenRequest body = GitlabTokenRequest.builder()
                .clientId(CLIENT_ID)
                .clientSecret(CLIENT_SECRET)
                .code(code)
                .grantType(GitlabGrantType.AUTHORIZATION_CODE.name().toLowerCase())
                .redirectUri(REDIRECT_URI)
                .build();

        HttpEntity<GitlabTokenRequest> entity = new HttpEntity<>(body, headers);
        return restTemplate.postForObject(ACCESS_TOKEN_URL, entity, GitlabTokenResponse.class);
    }

    //https://docs.gitlab.com/ee/api/projects.html
    public List<GitlabRepository> getRepositories(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<GitlabRepository[]> response = restTemplate.exchange(BASE_URL + "/projects?membership=true", HttpMethod.GET, entity, GitlabRepository[].class);
        return response.getBody() == null ? List.of() : List.of(response.getBody());
    }

    //https://docs.gitlab.com/ee/api/branches.html , https://gitlab.com/api/v4/projects/48908504/repository/branches
    public List<GitlabBranch> getBranches(String accessToken, String repoId) {
        String url = String.format("%s/projects/%s/repository/branches", BASE_URL, repoId);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<GitlabBranch[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, GitlabBranch[].class);
        return response.getBody() == null ? List.of() : List.of(response.getBody());
    }
}
