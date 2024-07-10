package com.project.workaholic.project.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IssueConfigDto {
    @Schema(description = "이슈 아이디")
    private Long id;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    @Schema(description = "이슈 이름")
    private String name;

    @Schema( description = "이슈 라벨링")
    private String label;

    @Schema(description = "이슈 상태")
    private String status;

    @Schema(description = "이슈 담당자")
    private String assignees;

    @Schema(description = "이슈 관련 내용 및 설명")
    private String description;

    @Schema(description = "이슈 해결을 위한 부가적인 첨부파일")
    private String attachments;

    @Schema(description = "이슈 환경 구축을 위한 이미지(=브랜치)")
    private String development;

    @Schema(description = "프로젝트 아이디(=스프린트)")
    private String projectId;

    @Schema(description = "보고자")
    private String reporter;

    @Schema(description = "이슈 시작일")
    private LocalDateTime startDate;

    @Schema(description = "이슈 종료일")
    private LocalDateTime dueDate;

    @Schema(description = "이슈 생성일자")
    private LocalDateTime createAt;

    @Schema(description = "이슈 수정일자")
    private LocalDateTime modifyAt;

    @Builder
    public IssueConfigDto(Long id, String name, String label, String status, String assignees, String description, String attachments, String development, String projectId, String reporter, LocalDateTime startDate, LocalDateTime dueDate, LocalDateTime createAt, LocalDateTime modifyAt) {
        this.id = id;
        this.name = name;
        this.label = label;
        this.status = status;
        this.assignees = assignees;
        this.description = description;
        this.attachments = attachments;
        this.development = development;
        this.projectId = projectId;
        this.reporter = reporter;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.createAt = createAt;
        this.modifyAt = modifyAt;
    }
}
