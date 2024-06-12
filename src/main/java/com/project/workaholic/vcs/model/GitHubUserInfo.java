package com.project.workaholic.vcs.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class GitHubUserInfo {
    @Schema(description = "로그인 이름")
    private final String loginId;

    @Schema(description = "Profile 설정된 이름")
    private final String name;

    @Schema(description = "등록한 Mail 주소")
    private final String mail;

    @Schema(description = "등록된 Repository 확인 가능한 Url")
    private final String repoUrl;

    @JsonCreator
    public GitHubUserInfo(
            @JsonProperty("login")String loginId,
            @JsonProperty("name")String name,
            @JsonProperty("email")String mail,
            @JsonProperty("repos_url")String repoUrl) {
        this.loginId = loginId;
        this.name = name;
        this.mail = mail;
        this.repoUrl = repoUrl;
    }
}
