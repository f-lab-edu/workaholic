package com.project.kubernetes.config;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

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

    @Bean
    public KubernetesClient kubernetesClient() {
        return new KubernetesClientBuilder().build();
    }
}
