package com.project.workaholic.project.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkProjectUpdateConfigReq {
    @Schema(description = "프로젝트 이름")
    private String name;

    @Builder
    public WorkProjectUpdateConfigReq(String name) {
        this.name = name;
    }
}
