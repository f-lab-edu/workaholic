package com.project.workaholic.project.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.project.workaholic.config.validation.ValidEnum;
import com.project.workaholic.project.model.enumeration.IssueStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
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
