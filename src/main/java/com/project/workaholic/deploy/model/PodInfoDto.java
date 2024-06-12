package com.project.workaholic.deploy.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PodInfoDto {
    @Schema(name = "이름", description = "Pod 이름", example = "pod1")
    private String name;

    @Schema(name = "Pod 상태", description = "Pod 상태", example = "Running")
    private String status;

    @Schema(name = "네임스페이스", description = "Pod 의 Name space", example = "default")
    private String namespace;

    @Schema(name = "생성 시간", description = "Pod 가 생성된 시간")
    private LocalDateTime createAt;

    @Builder
    public PodInfoDto(String name, String status, String namespace, LocalDateTime createAt) {
        this.name = name;
        this.status = status;
        this.namespace = namespace;
        this.createAt = createAt;
    }
}
