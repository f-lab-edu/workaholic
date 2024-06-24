package com.project.workaholic.vcs.vendor.github.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GithubRepository {
    @Schema(description = "고유 아이디")
    private Long id;

    @Schema(description = "이름")
    private String name;

    @Schema(description = "풀 네임(소유자/리포지토리_이름)")
    private String fullName;

    @Schema(description = "설명")
    private String description;

    @Schema(description = "private 리포지토리 여부")
    private String isPrivate;

    @Schema(description = "API URL")
    private String url;

    @Schema(description = "웹 URL")
    private String htmlUrl;

    @Schema(description = "본 리포지토리 commit 정보 가져오기 위한 API URL")
    private String commitsUrl;

    @Schema(description = "본 리포지토리 branch 정보 가져오기 위한 API URL")
    private String branchesUrl;
}
