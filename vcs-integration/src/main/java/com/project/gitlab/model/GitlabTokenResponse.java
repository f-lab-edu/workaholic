package com.project.gitlab.model;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GitlabTokenResponse {
    private final String accessToken;
    private final String tokenType;
    private final int expiresIn;
    private final String refreshToken;
    private final int createdAt;

    @Builder
    public GitlabTokenResponse(String accessToken, String tokenType, int expiresIn, String refreshToken, int createdAt) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
        this.refreshToken = refreshToken;
        this.createdAt = createdAt;
    }
}
