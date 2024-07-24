package com.project.workaholic.project.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkProjectConfigResDto {
    @Schema(description = "프로젝트 이름")
    private String name;

    @Schema(description = "레포지토리 이름")
    private String repositoryName;

    @Schema(description = "레포지토리 URL")
    private String repoUrl;

    @Schema(description = "브랜치 목록")
    private List<String> branches;

    @Schema(description = "latest commit")
    private String latestCommit;

    @Schema(description = "Project 설정 값")
    private WorkProjectConfiguration configuration;

    public WorkProjectConfigResDto(String name, String repositoryName, String repoUrl, List<String> branches, String latestCommit, WorkProjectConfiguration configuration) {
        this.name = name;
        this.repositoryName = repositoryName;
        this.repoUrl = repoUrl;
        this.branches = branches;
        this.latestCommit = latestCommit;
        this.configuration = configuration;
    }
}
