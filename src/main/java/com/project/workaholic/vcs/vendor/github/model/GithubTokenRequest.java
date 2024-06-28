package com.project.workaholic.vcs.vendor.github.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GithubTokenRequest {
    @Schema(description = "Github Access Token 요청을 위한 client_id")
    @JsonProperty("client_id")
    private String clientId;

    @Schema(description = "Github Access Token 요청을 위한 client_secret")
    @JsonProperty("client_secret")
    private String clientSecret;

    @Schema(description = "Github Access Token 요청을 위한 인증 코드")
    @JsonProperty("code")
    private String code;

    @Schema(description = "Github Redirect URI")
    @JsonProperty("redirect_uri")
    private String redirectUri;

    public GithubTokenRequest(String clientId, String clientSecret, String code, String redirectUri) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.code = code;
        this.redirectUri = redirectUri;
    }
}
