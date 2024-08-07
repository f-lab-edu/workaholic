package com.project.workaholic.vcs.vendor.github.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class GithubBranch {
    @Schema(description = "이름")
    private String name;

    @Schema(description = "커밋 정보")
    private Commit commit;

    @Getter
    private static class Commit {
        @Schema(description = "SHA 해쉬값")
        private String sha;

        @Schema(description = "커밋 URL")
        private String url;
    }
}
