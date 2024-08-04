package com.project.deploy.api;


import com.project.deploy.service.DeployService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            final UUID id) {
        // 생성된 Image pod deploy
        String kubeNamespace = deployService.getNamespaceByProjectName(createdWorkProject.getOwner());
        deployService.createPod(kubeNamespace, id,"nginx:latest");

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
