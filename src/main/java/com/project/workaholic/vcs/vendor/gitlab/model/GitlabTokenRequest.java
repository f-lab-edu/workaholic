package com.project.workaholic.vcs.vendor.gitlab.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GitlabTokenRequest {
    @Schema(description = "Gitlab Access Token 요청을 위한 client_id")
    private String clientId;

    @Schema(description = "Gitlab Access Token 요청을 위한 client_secret")
    private String clientSecret;

    @Schema(description = "Gitlab Access Token 요청을 위한 인증 코드")
    private String code;

    @Schema(description = "Gitlab Redirect URI")
    private String redirectUri;

    @Schema(description = "요청의 유형 선택")
    private String grantType;

    @Builder
    public GitlabTokenRequest(String clientId, String clientSecret, String code, String redirectUri, String grantType) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.code = code;
        this.redirectUri = redirectUri;
        this.grantType = grantType;
    }
}
