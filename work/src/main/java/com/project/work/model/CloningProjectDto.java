package com.project.work.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CloningProjectDto {
    @NotBlank
    private String id;

    @NotBlank
    private String repositoryUrl;

    public CloningProjectDto(String id, String repositoryUrl) {
        this.id = id;
        this.repositoryUrl = repositoryUrl;
    }
}


