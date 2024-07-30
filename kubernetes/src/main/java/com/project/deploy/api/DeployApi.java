package com.project.deploy.api;


import com.project.deploy.service.DeployService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
