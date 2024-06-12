package com.project.workaholic.vcs.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GitlabAccessTokenResponseDto {
    @Schema(description = "Access Token")
    private final String accessToken;

    @Schema(description = "token_type")
    private final String tokenType;

    @Schema(description = "액세스 토큰 유효기간")
    private final long accessExpires;

    @Schema(description = "Refresh Token")
    private final String refreshToken;

    @Schema(description = "토큰 생성 시간")
    private final LocalDateTime createAt;

    @JsonCreator
    public GitlabAccessTokenResponseDto(
            @JsonProperty("access_token") String accessToken,
            @JsonProperty("token_type") String tokenType,
            @JsonProperty("expires_in")long accessExpires,
            @JsonProperty("refresh_token") String refreshToken,
            @JsonProperty("created_at") LocalDateTime createAt) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.accessExpires = accessExpires;
        this.refreshToken = refreshToken;
        this.createAt = createAt;
    }
}
