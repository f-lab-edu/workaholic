package com.project.workaholic.vcs.vendor.gitlab.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
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
