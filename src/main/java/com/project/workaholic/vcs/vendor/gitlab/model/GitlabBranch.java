package com.project.workaholic.vcs.vendor.gitlab.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GitlabBranch {
    @Schema(description = "이름")
    private String name;

    @Schema(description = "커밋 정보")
    private Commit commit;

    @Getter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    private static class Commit {
        @Schema(description = "SHA 해쉬값")
        private String id;

        @Schema(description = "커밋 URL")
        private String web_url;

        @Schema(description = "title")
        private String title;
    }
}
