package com.project.workaholic.vcs.vendor.github.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class GithubAccessTokenResponseDto {
    @Schema(description = "Access Token")
    private final String accessToken;

    @Schema(description = "scope")
    private final String scope;

    @Schema(description = "token_type")
    private final String tokenType;

    @JsonCreator
    public GithubAccessTokenResponseDto(
            @JsonProperty("access_token") String accessToken,
            @JsonProperty("scope") String scope,
            @JsonProperty("token_type") String tokenType) {
        this.accessToken = accessToken;
        this.scope = scope;
        this.tokenType = tokenType;
    }
}
