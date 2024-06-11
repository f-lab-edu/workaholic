package com.project.workaholic.vcs.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class GitHubUserInfo {
    private final String id;
    private final String userName;
    private final String nickName;

    @JsonCreator
    public GitHubUserInfo(
            @JsonProperty("id") String id, @JsonProperty("id") String userName, @JsonProperty("name") String nickName) {
        this.id = id;
        this.userName = userName;
        this.nickName = nickName;
    }
}
