package com.project.workaholic.issue.model;

import com.project.workaholic.config.validation.ValidEnum;
import com.project.workaholic.issue.model.enumeration.IssueStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IssueInfoDto {
    @Schema(description = "이슈 이름")
    private String name;

    @ValidEnum(enumClass = IssueStatus.class)
    @Schema(description = "이슈 상태")
    private IssueStatus status;

    @Schema(description = "이슈 배정자")
    private String assignees;

    @Builder
    public IssueInfoDto(String name, IssueStatus status, String assignees) {
        this.name = name;
        this.status = status;
        this.assignees = assignees;
    }
}
