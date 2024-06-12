package com.project.workaholic.deploy.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PodConfigDto {
    private String name;
    private String image;

    @Builder
    public PodConfigDto(String name, String image) {
        this.name = name;
        this.image = image;
    }
}
