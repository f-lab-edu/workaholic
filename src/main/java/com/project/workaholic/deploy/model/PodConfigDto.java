package com.project.workaholic.deploy.model;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PodConfigDto {
    private String name;
    private String image;

    @Builder
    public PodConfigDto(String name, String image) {
        this.name = name;
        this.image = image;
    }
}
