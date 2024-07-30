package com.project.gitlab.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.gitlab.model.enumeration.GitlabGrantType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GitlabTokenRequest {
    @JsonProperty("client_id")
    private String clientId;
    @JsonProperty("client_secret")
    private String clientSecret;
    @JsonProperty("code")
    private String code;
    @JsonProperty("redirect_uri")
    private String redirectUri;
    @JsonProperty("grant_type")
    private String grantType;

    public GitlabTokenRequest(String clientId, String clientSecret, String code, String redirectUri, GitlabGrantType grantType) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.code = code;
        this.redirectUri = redirectUri;
        this.grantType = grantType.name().toLowerCase();
    }
}
