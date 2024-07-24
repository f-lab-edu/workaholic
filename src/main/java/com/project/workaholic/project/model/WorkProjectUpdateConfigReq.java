package com.project.workaholic.project.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkProjectUpdateConfigReq {
    @Schema(description = "프로젝트 이름")
    private String name;

    @Schema(description = "Project 설정 값")
    private WorkProjectConfiguration configuration;

    public WorkProjectUpdateConfigReq(String name, WorkProjectConfiguration configuration) {
        this.name = name;
        this.configuration = configuration;
    }
}
