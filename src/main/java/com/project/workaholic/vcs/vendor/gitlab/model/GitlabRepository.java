package com.project.workaholic.vcs.vendor.gitlab.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GitlabRepository {
    //https://docs.gitlab.com/ee/api/projects.html#get-single-project
    @Schema(description = "아이디")
    private String id;

    @Schema(description = "이름")
    private String name;

    @Schema(description = "풀 네임(네임스페이스/리포지토리_이름)")
    private String path_with_namespace;

    @Schema(description = "설명")
    private String description;

    @Schema(description = "private 리포지토리 여부")
    private String visibility;

    @Schema(description = "웹 URL")
    private String web_url;
}
