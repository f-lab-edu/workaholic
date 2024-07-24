package com.project.workaholic.project.model;

import com.project.workaholic.vcs.model.enumeration.VCSVendor;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkProjectConfigReqDto {

    @Schema(description = "프로젝트 이름")
    private String name;

    @Schema(description = "설명")
    private String description;

    @Schema(description = "레포지토리 이름")
    private String repositoryName;

    @Schema(description = "버전관리 Vendor")
    private VCSVendor vendor;

    @Schema(description = "Project 설정 값")
    private WorkProjectConfiguration configuration;
}
