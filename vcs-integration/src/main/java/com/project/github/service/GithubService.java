package com.project.github.service;

import com.project.exception.type.FailedCloneRepositoryException;
import com.project.exception.type.FailedCreateDirectory;
import com.project.github.model.GithuTokenResponse;
import com.project.github.model.GithubBranch;
import com.project.github.model.GithubRepository;
import com.project.github.model.GithubTokenRequest;
import com.project.oauth.model.VCSRepository;
import com.project.oauth.model.entity.OAuthAccessToken;
import com.project.oauth.model.enumeration.VCSVendor;
import com.project.oauth.repository.OAuthAccessTokenRepository;
import com.project.oauth.service.VendorApiService;
import com.project.oauth.service.VendorManager;
import jakarta.annotation.PostConstruct;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.Arrays;
import java.util.List;

@Service
public class GithubService implements VendorApiService {
    private static final String BASE_DIR = "C:\\Users\\Tmax\\Desktop";

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

    public String cloneRepository(String workId, String cloneUrl, String token) throws FailedCloneRepositoryException {
        File directory = new File(BASE_DIR, workId);

        if (!directory.exists()) {
            boolean isCreated = directory.mkdirs();
            if (!isCreated) {
                throw new FailedCreateDirectory(directory.getAbsolutePath());
            }
        }

        try{
            Git.cloneRepository().setURI(cloneUrl)
                    .setDirectory(directory)
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider(token, ""))
                    .call();
        } catch (GitAPIException e) {
            throw new FailedCloneRepositoryException(cloneUrl);
        }

        return directory.getAbsolutePath();
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
