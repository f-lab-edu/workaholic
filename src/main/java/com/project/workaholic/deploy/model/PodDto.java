package com.project.workaholic.deploy.model;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PodDto {
    private String name;
    private String status;
    private String namespace;

    public PodDto(String name, String status, String namespace) {
        this.name = name;
        this.status = status;
        this.namespace = namespace;
    }
}
