package com.project.gitlab.service;

import com.project.gitlab.model.GitlabBranch;
import com.project.gitlab.model.GitlabRepository;
import com.project.gitlab.model.GitlabTokenRequest;
import com.project.gitlab.model.GitlabTokenResponse;
import com.project.gitlab.model.enumeration.GitlabGrantType;
import com.project.oauth.model.VCSRepository;
import com.project.oauth.model.entity.OAuthAccessToken;
import com.project.oauth.model.enumeration.VCSVendor;
import com.project.oauth.repository.OAuthAccessTokenRepository;
import com.project.oauth.service.VendorApiService;
import com.project.oauth.service.VendorManager;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class GitlabService implements VendorApiService {
    private final OAuthAccessTokenRepository oAuthAccessTokenRepository;
    private final VendorManager vendorManager;
    private final GitlabProperties properties;
    private final RestTemplate restTemplate;

    public GitlabService(OAuthAccessTokenRepository oAuthAccessTokenRepository, VendorManager vendorManager, GitlabProperties properties, RestTemplate restTemplate) {
        this.oAuthAccessTokenRepository = oAuthAccessTokenRepository;
        this.vendorManager = vendorManager;
        this.properties = properties;
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    @Override
    public void init() {
        vendorManager.registerService(VCSVendor.GITLAB, this);
    }

    @Override
    public OAuthAccessToken getOAuthAccessTokenByAccountId(String accountId) {
        return oAuthAccessTokenRepository.findGitlabByAccountId(accountId).orElse(null);
    }

    @Override
    public VCSRepository getRepositoryInformation(String accessToken, String repositoryName) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<GitlabRepository> response = restTemplate.exchange(properties.getBASE_URL() + "/projects?membership=true", HttpMethod.GET, entity, GitlabRepository.class);
        return response.getBody();
    }

    public GitlabTokenResponse getAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        GitlabTokenRequest body = new GitlabTokenRequest(properties.getCLIENT_ID(), properties.getCLIENT_SECRET(), code, properties.getREDIRECT_URI(), GitlabGrantType.AUTHORIZATION_CODE);

        HttpEntity<GitlabTokenRequest> entity = new HttpEntity<>(body, headers);
        return restTemplate.postForObject(properties.getACCESS_TOKEN_URL(), entity, GitlabTokenResponse.class);
    }

    //https://docs.gitlab.com/ee/api/projects.html
    public List<String> getRepositoryNames(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<GitlabRepository[]> response = restTemplate.exchange(properties.getBASE_URL() + "/projects?membership=true", HttpMethod.GET, entity, GitlabRepository[].class);
        return response.getBody() == null ? List.of() : Arrays.stream(response.getBody()).map(GitlabRepository::getName).toList();
    }

    //https://docs.gitlab.com/ee/api/branches.html , https://gitlab.com/api/v4/projects/48908504/repository/branches
    public List<GitlabBranch> getBranches(String accessToken, String repoId) {
        String url = String.format("%s/projects/%s/repository/branches", properties.getBASE_URL(), repoId);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<GitlabBranch[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, GitlabBranch[].class);
        return response.getBody() == null ? List.of() : List.of(response.getBody());
    }
}
