package com.project.workaholic.vcs.vendor.gitlab.service;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "oauth2.gitlab")
public class GitlabProperties {
    private final String BASE_URL;
    private final String ACCESS_TOKEN_URL;
    private final String CLIENT_ID;
    private final String CLIENT_SECRET;
    private final String REDIRECT_URI;

    public GitlabProperties(String baseUrl, String accessTokenUrl, String clientId, String clientSecret, String redirectUri) {
        BASE_URL = baseUrl;
        ACCESS_TOKEN_URL = accessTokenUrl;
        CLIENT_ID = clientId;
        CLIENT_SECRET = clientSecret;
        REDIRECT_URI = redirectUri;
    }
}
