package com.project.github.service;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "oauth2.github")
public class GithubProperties {
    private final String BASE_URL;
    private final String ACCEPT;
    private final String ACCESS_TOKEN_URL;
    private final String CLIENT_ID;
    private final String CLIENT_SECRET;

    public GithubProperties(String BASE_URL, String ACCEPT, String ACCESS_TOKEN_URL, String CLIENT_ID, String CLIENT_SECRET) {
        this.BASE_URL = BASE_URL;
        this.ACCEPT = ACCEPT;
        this.ACCESS_TOKEN_URL = ACCESS_TOKEN_URL;
        this.CLIENT_ID = CLIENT_ID;
        this.CLIENT_SECRET = CLIENT_SECRET;
    }
}
