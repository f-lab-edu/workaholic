package com.project.workaholic.deploy.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KubernetesResponse {
    private String kind;
    private List<Object> items = new ArrayList<>();
}
