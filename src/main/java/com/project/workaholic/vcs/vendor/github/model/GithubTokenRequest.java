package com.project.workaholic.vcs.vendor.github.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GithubTokenRequest {
    @Schema(description = "Github Access Token 요청을 위한 client_id")
    private String clientId;

    @Schema(description = "Github Access Token 요청을 위한 client_secret")
    private String clientSecret;

    @Schema(description = "Github Access Token 요청을 위한 인증 코드")
    private String code;

    @Builder
    public GithubTokenRequest(String clientId, String clientSecret, String code) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.code = code;
    }
}
