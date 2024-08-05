package com.project.deploy.api;


import com.project.deploy.model.entity.KubeNamespace;
import com.project.deploy.service.DeployService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/container")
public class DeployApi {
    private final DeployService deployService;

    public DeployApi(DeployService deployService) {
        this.deployService = deployService;
    }

    @GetMapping("/pod/{namespace}")
    public ResponseEntity<List<String>> getPods(
            final @PathVariable String namespace) {
        List<String> pods = deployService.getPodByNamespace(namespace);
        return ResponseEntity.status(HttpStatus.OK)
                .body(pods);
    }

    @DeleteMapping("/pod/{namespace}")
    public ResponseEntity<Void> deleteDeployment(
            final @PathVariable String namespace,
            final @RequestParam("id") String podName) {
        deployService.removePod(namespace, podName);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/pod")
    public ResponseEntity<Void> createPod(
            final UUID id,
            final String name) {
        // 생성된 Image pod deploy
        KubeNamespace kubeNamespace = deployService.getNamespaceByProjectName(name);
        deployService.createPod(kubeNamespace, id,"nginx:latest");

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
