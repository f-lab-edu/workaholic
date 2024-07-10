package com.project.workaholic.vcs.vendor.github.service;

import com.project.workaholic.vcs.model.VCSRepository;
import com.project.workaholic.vcs.model.entity.OAuthAccessToken;
import com.project.workaholic.vcs.model.enumeration.VCSVendor;
import com.project.workaholic.vcs.repository.OAuthAccessTokenRepository;
import com.project.workaholic.vcs.service.VendorApiService;
import com.project.workaholic.vcs.service.VendorManager;
import com.project.workaholic.vcs.vendor.github.model.*;
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
public class GithubService implements VendorApiService {
    private final OAuthAccessTokenRepository oAuthAccessTokenRepository;
    private final VendorManager vendorManager;
    private final GithubProperties properties;
    private final RestTemplate restTemplate;

    public GithubService(OAuthAccessTokenRepository oAuthAccessTokenRepository, VendorManager vendorManager, GithubProperties properties, RestTemplate restTemplate) {
        this.oAuthAccessTokenRepository = oAuthAccessTokenRepository;
        this.vendorManager = vendorManager;
        this.properties = properties;
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    @Override
    public void init() {
        vendorManager.registerService(VCSVendor.GITHUB, this);
    }

    @Override
    public OAuthAccessToken getOAuthAccessTokenByAccountId(String accountId) {
        return oAuthAccessTokenRepository.findGithubByAccountId(accountId).orElse(null);
    }

    @Override
    public VCSRepository getRepositoryInformation(String accessToken, String repositoryName) {
        HttpHeaders headers = getHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<GithubRepository> response = restTemplate.exchange(properties.getBASE_URL() +"/repos/" + repositoryName, HttpMethod.GET, entity, GithubRepository.class);
        return response.getBody();
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, properties.getACCEPT());
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
        headers.add("X-GitHub-Api-Version", "2022-11-28");

        return headers;
    }

    public GithuTokenResponse getAccessToken(String code) {
        HttpHeaders headers = getHeaders();
        GithubTokenRequest body = new GithubTokenRequest(properties.getCLIENT_ID(), properties.getCLIENT_SECRET(), code, "");

        HttpEntity<GithubTokenRequest> entity = new HttpEntity<>(body, headers);
        return restTemplate.postForObject(properties.getACCESS_TOKEN_URL(), entity, GithuTokenResponse.class);
    }

    //https://docs.github.com/ko/rest/repos/repos?apiVersion=2022-11-28#list-repositories-for-the-authenticated-user
    public List<String> getRepositoryNames(String accessToken) {
        HttpHeaders headers = getHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<GithubRepository[]> response = restTemplate.exchange(properties.getBASE_URL() +"/user/repos", HttpMethod.GET, entity, GithubRepository[].class);

        return response.getBody() == null ? List.of() :  Arrays.stream(response.getBody()).map(GithubRepository::getFullName).toList();
    }

    //https://docs.github.com/ko/rest/branches/branches?apiVersion=2022-11-28#list-branches
    public List<GithubBranch> getBranches(String accessToken, String owner, String repo) {
        String url = String.format("%s/repos/%s/%s/branches", properties.getBASE_URL(), owner, repo);
        HttpHeaders headers = getHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<GithubBranch[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, GithubBranch[].class);
        return response.getBody() == null ? List.of() :  List.of(response.getBody());
    }
}
