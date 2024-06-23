package com.project.workaholic.vcs.vendor.gitlab.service;

import com.project.workaholic.vcs.vendor.gitlab.model.GitlabBranch;
import com.project.workaholic.vcs.vendor.gitlab.model.GitlabRepository;
import com.project.workaholic.vcs.vendor.gitlab.model.GitlabTokenRequest;
import com.project.workaholic.vcs.vendor.gitlab.model.GitlabTokenResponse;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.UUID;

@ConfigurationProperties(prefix = "oauth2.gitlab")
public class GitlabService {
    private final String BASE_URL;
    private final String LOGIN_URL;
    private final String ACCESS_TOKEN_URL;
    private final String CLIENT_ID;
    private final String CLIENT_SECRET;
    private final String REDIRECT_URL;
    private final RestTemplate restTemplate;

    public GitlabService(String BASE_URL, String LOGIN_URL, String ACCESS_TOKEN_URL, String CLIENT_ID, String CLIENT_SECRET, String REDIRECT_URL) {
        this.BASE_URL = BASE_URL;
        this.LOGIN_URL = LOGIN_URL;
        this.ACCESS_TOKEN_URL = ACCESS_TOKEN_URL;
        this.CLIENT_ID = CLIENT_ID;
        this.CLIENT_SECRET = CLIENT_SECRET;
        this.REDIRECT_URL = REDIRECT_URL;
        this.restTemplate = new RestTemplate();
    }

    //https://docs.gitlab.com/ee/api/oauth2.html#authorization-code-with-proof-key-for-code-exchange-pkce
    public RedirectView requestCode(RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("client_id", CLIENT_ID)
                .addAttribute("redirect_uri", REDIRECT_URL)
                .addAttribute("state", UUID.randomUUID().toString());

        return new RedirectView(LOGIN_URL);
    }


    public GitlabTokenResponse getAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        GitlabTokenRequest body = GitlabTokenRequest.builder()
                .clientId(CLIENT_ID)
                .clientSecret(CLIENT_SECRET)
                .code(code)
                .build();

        HttpEntity<GitlabTokenRequest> entity = new HttpEntity<>(body, headers);
        return restTemplate.postForObject(ACCESS_TOKEN_URL, entity, GitlabTokenResponse.class);
    }

    //https://docs.gitlab.com/ee/api/repositories.html
    public GitlabRepository getRepositories(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("PRIVATE-TOKEN", accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.getForObject(BASE_URL + "/projects", GitlabRepository.class, entity);
    }

    //https://docs.gitlab.com/ee/api/branches.html
    public GitlabBranch getBranches(String accessToken, String repo) {
        String url = String.format("%s/projects/%s/repository/branches", BASE_URL, repo);
        HttpHeaders headers = new HttpHeaders();
        headers.add("PRIVATE-TOKEN", accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.getForObject(url, GitlabBranch.class, entity);
    }
}
