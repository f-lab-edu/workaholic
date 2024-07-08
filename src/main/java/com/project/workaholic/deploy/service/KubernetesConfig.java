package com.project.workaholic.deploy.service;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "kubernetes")
public class KubernetesConfig {
    private final String url;
    private final String token;
    private final String baseUrl;

    public KubernetesConfig(String url, String token, String baseUrl) {
        this.url = url;
        this.token = token;
        this.baseUrl = baseUrl;
    }
}
