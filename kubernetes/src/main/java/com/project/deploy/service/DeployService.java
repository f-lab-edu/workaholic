package com.project.deploy.service;

import com.project.deploy.model.entity.KubeNamespace;
import com.project.deploy.repository.KubeNamespaceRepository;
import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.NamespaceBuilder;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodBuilder;
import io.fabric8.kubernetes.api.model.StatusDetails;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DeployService {
    private final KubernetesClient kubernetesClient;
    private final KubeNamespaceRepository namespaceRepository;
    private final KubeNamespaceRepository kubeNamespaceRepository;

    public DeployService(KubernetesClient kubernetesClient, KubeNamespaceRepository namespaceRepository, KubeNamespaceRepository kubeNamespaceRepository) {
        this.kubernetesClient = kubernetesClient;
        this.namespaceRepository = namespaceRepository;
        this.kubeNamespaceRepository = kubeNamespaceRepository;
    }

    public List<String> getPodByNamespace(String namespace) {
        return kubernetesClient.pods().inNamespace(namespace).list().getItems().stream()
                .map(Pod::getMetadata)
                .map(ObjectMeta::getName)
                .collect(Collectors.toList());
    }

    public void createPod(String namespace, UUID projectId, String imageName) {
        //namespace == accountID , podName == projectName, imageName == TODO
        Pod pod = new PodBuilder()
                .withNewMetadata().withName(projectId.toString()).endMetadata()
                .withNewSpec()
                .addNewContainer()
                .withName(projectId.toString())
                .withImage(imageName)
                .endContainer()
                .endSpec().build();

        kubernetesClient.pods()
                .inNamespace(namespace)
                .resource(pod)
                .create();
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

    public void createNamespaceByAccountId(String accountId) {
        KubeNamespace kubeNamespace = new KubeNamespace(accountId);
        Namespace namespace = new NamespaceBuilder()
                .withNewMetadata()
                .withName(kubeNamespace.getId().toString())
                .endMetadata()
                .build();

        kubernetesClient.namespaces().resource(namespace).create();
        namespaceRepository.save(kubeNamespace);
    }

    public List<StatusDetails> removeNamespaceByAccountId(String accountId) {
        return kubernetesClient.namespaces().withName(accountId).delete();
    }

    public KubeNamespace getNamespaceByProjectName(String projectName) {
        return kubeNamespaceRepository.findNamespaceByAccountId(projectName);
    }
}
