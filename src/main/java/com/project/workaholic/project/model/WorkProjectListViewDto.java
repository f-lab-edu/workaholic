package com.project.workaholic.project.model;

import com.project.workaholic.vcs.model.enumeration.VCSVendor;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkProjectListViewDto {
    @Schema(description = "프로젝트 아이디")
    private String id;

    @Schema(description = "프로젝트 이름")
    private String name;

    @Schema(description = "VCS Vendor")
    private VCSVendor vendor;

    @Schema(description = "레포지토리 이름")
    private String repositoryName;

    @Schema(description = "최근 Commit")
    private String commit;

    @Schema(description = "배포된 branch")
    private String branch;

    public WorkProjectListViewDto(String id, String name, VCSVendor vendor, String repositoryName, String commit, String branch) {
        this.id = id;
        this.name = name;
        this.vendor = vendor;
        this.repositoryName = repositoryName;
        this.commit = commit;
        this.branch = branch;
    }
}
