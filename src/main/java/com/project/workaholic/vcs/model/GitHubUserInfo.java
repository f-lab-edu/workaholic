package com.project.workaholic.vcs.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class GitHubUserInfo {
    private final String loginId;
    private final String name;
    private final String mail;
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
