package com.project.workaholic.vcs.vendor.gitlab.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.workaholic.vcs.vendor.gitlab.model.enumeration.GitlabGrantType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GitlabTokenRequest {
    @Schema(description = "Gitlab Access Token 요청을 위한 client_id")
    @JsonProperty("client_id")
    private String clientId;

    @Schema(description = "Gitlab Access Token 요청을 위한 client_secret")
    @JsonProperty("client_secret")
    private String clientSecret;

    @Schema(description = "Gitlab Access Token 요청을 위한 인증 코드")
    @JsonProperty("code")
    private String code;

    @Schema(description = "Gitlab Redirect URI")
    @JsonProperty("redirect_uri")
    private String redirectUri;

    @Schema(description = "요청의 유형 선택")
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
