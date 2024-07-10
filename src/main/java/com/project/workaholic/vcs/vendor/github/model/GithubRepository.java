package com.project.workaholic.vcs.vendor.github.model;

import com.project.workaholic.vcs.model.VCSRepository;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class GithubRepository implements VCSRepository {
    @Schema(description = "이름")
    private String name;

    @Schema(description = "Full Name")
    private String fullName;

    @Schema(description = "API URL")
    private String url;

    @Schema(description = "웹 URL")
    private String htmlUrl;

    @Schema(description = "클론 URL")
    private String cloneUrl;

    @Schema(description = "본 리포지토리 commit 정보 가져오기 위한 API URL")
    private String commitsUrl;

    @Schema(description = "본 리포지토리 branch 정보 가져오기 위한 API URL")
    private String branchesUrl;

    @Override
    public String getApiUrl() {
        return this.url;
    }
}
