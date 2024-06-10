package com.project.workaholic.vcs.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuthGithubAccessTokenResponseDto {
    @Schema(description = "Access Token")
    @JsonProperty("access_token")
    private String accessToken;

    @Schema(description = "Access Token 유효 기간")
    @JsonProperty("expires_in")
    private Long accessTokenExpiresIn;

    @Schema(description = "갱신을 위한 Refresh Token")
    @JsonProperty("refresh_token")
    private String refreshToken;

    @Schema(description = "Refresh Token 유효 기간")
    @JsonProperty("refresh_token_expires_in")
    private Long refreshTokenExpiresIn;
}
