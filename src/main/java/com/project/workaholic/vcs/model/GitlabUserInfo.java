package com.project.workaholic.vcs.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class GitlabUserInfo {
    @Schema(description = "로그인 이름")
    private final String loginId;

    @Schema(description = "Profile 설정된 이름")
    private final String name;

    @Schema(description = "등록한 Mail 주소")
    private final String mail;

    @JsonCreator
    public GitlabUserInfo(
            @JsonProperty("username")String loginId,
            @JsonProperty("name")String name,
            @JsonProperty("email")String mail) {
        this.loginId = loginId;
        this.name = name;
        this.mail = mail;
    }
}
