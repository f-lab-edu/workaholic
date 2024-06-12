package com.project.workaholic.vcs.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class OAuthGithubAccessTokenResponseDto {
    @Schema(description = "Access Token")
    private final String accessToken;

    @Schema(description = "Access Token 유효 기간")
    private final Long accessTokenExpiresIn;

    @Schema(description = "갱신을 위한 Refresh Token")
    private final String refreshToken;

    @Schema(description = "Refresh Token 유효 기간")
    private final Long refreshTokenExpiresIn;

    @Schema(description = "scope")
    private final String scope;

    @Schema(description = "token_type")
    private final String tokenType;

    @JsonCreator
    public OAuthGithubAccessTokenResponseDto(
            @JsonProperty("access_token")String accessToken,
            @JsonProperty("expires_in")Long accessTokenExpiresIn,
            @JsonProperty("refresh_token")String refreshToken,
            @JsonProperty("refresh_token_expires_in")Long refreshTokenExpiresIn,
            @JsonProperty("scope")String scope,
            @JsonProperty("token_type")String tokenType) {
        this.accessToken = accessToken;
        this.accessTokenExpiresIn = accessTokenExpiresIn;
        this.refreshToken = refreshToken;
        this.refreshTokenExpiresIn = refreshTokenExpiresIn;
        this.scope = scope;
        this.tokenType = tokenType;
    }
}
