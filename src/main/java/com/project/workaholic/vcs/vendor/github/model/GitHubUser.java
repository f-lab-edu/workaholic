package com.project.workaholic.vcs.vendor.github.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GitHubUser {
    @Schema(description = "로그인 이름")
    private String loginId;

    @Schema(description = "Profile 설정된 이름")
    private String name;

    @Schema(description = "등록한 Mail 주소")
    private String mail;
}
