package com.project.workaholic.deploy.service;

import com.project.workaholic.deploy.model.KubernetesResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DeployService {
    private final KubernetesConfig config;
    private final RestTemplate restTemplate;

    public DeployService(KubernetesConfig config, RestTemplate restTemplate) {
        this.config = config;
        this.restTemplate = restTemplate;
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(config.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        return headers;
    }

    public KubernetesResponse getPods() {
        HttpHeaders headers = getHeaders();
        String url = config.getUrl() + config.getBaseUrl() + "pods";

        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<KubernetesResponse> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, KubernetesResponse.class);
        return response.getBody();
    }
}
