package com.project.workaholic.vcs.vendor.gitlab.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GitlabTokenResponse {
    @Schema(description = "GitLab OAuth AccessToken 값")
    private final String accessToken;

    @Schema(description = "토큰 타입으로 거의 bearer 고정")
    private final String tokenType;

    @Schema(description = "엑세스 토큰 유효기간")
    private final int expiresIn;

    @Schema(description = "리프레쉬 토큰")
    private final String refreshToken;

    @Schema(description = "생성시간")
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
