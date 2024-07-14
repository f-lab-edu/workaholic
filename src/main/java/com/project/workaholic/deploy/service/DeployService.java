package com.project.workaholic.deploy.service;

import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodBuilder;
import io.fabric8.kubernetes.api.model.StatusDetails;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DeployService {
    private final KubernetesClient kubernetesClient;
    private final RestTemplate restTemplate;

    public DeployService(KubernetesClient kubernetesClient, RestTemplate restTemplate) {
        this.kubernetesClient = kubernetesClient;
        this.restTemplate = restTemplate;
    }

    public List<String> getPods(String namespace) {
        return kubernetesClient.pods().inNamespace(namespace).list().getItems().stream()
                .map(Pod::getMetadata)
                .map(ObjectMeta::getName)
                .collect(Collectors.toList());
    }

    public Pod createPod(String namespace, String podName, String imageName) {
        Pod pod = new PodBuilder()
                .withNewMetadata().withName(podName).endMetadata()
                .withNewSpec()
                .addNewContainer()
                .withName(podName)
                .withImage(imageName)
                .endContainer()
                .endSpec().build();

        return kubernetesClient.pods().inNamespace(namespace).resource(pod).create();
    }

    public Deployment updatePod(String namespace, String podName, String imageName, Map<String, String> envMap) {
        Deployment existingDeployment = kubernetesClient.apps().deployments().inNamespace(namespace).withName(podName).get();

        if(existingDeployment != null ) {
            // image update

            // 환경변수 업데이트
            if(envMap != null) {

            }
        } else {

        }
        return kubernetesClient.apps().deployments().inNamespace(namespace).withName(podName).get();
    }

    public List<StatusDetails> removePod(String namespace, String podName) {
        return kubernetesClient.pods().inNamespace(namespace).withName(podName).delete();
    }
}
