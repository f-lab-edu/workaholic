package kubernetes.deploy.service;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodBuilder;
import io.fabric8.kubernetes.api.model.ServiceBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.PodResource;
import io.fabric8.kubernetes.client.dsl.Resource;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class DeployService {
    private final KubernetesClient kubernetesClient;

    public DeployService(KubernetesClient kubernetesClient) {
        this.kubernetesClient = kubernetesClient;
    }

    private Pod createPod(String namespace, String imageName, int accessPort) {
        return new PodBuilder()
                .withNewMetadata()
                    .withNamespace(namespace)
                .endMetadata()
                .withNewSpec()
                    .addNewContainer()
                        .withImage(imageName)
                        .addNewPort()
                            .withContainerPort(accessPort) // Container's internal port
                        .endPort()
                    .endContainer()
                .endSpec()
                .build();
    }

    public void deployApplication(String namespace, String imageName, int port) {
        try {
            Pod createdPod = createPod(namespace, imageName, port);
            Resource<Pod> resource = kubernetesClient.pods()
                    .inNamespace(namespace).resource(createdPod);

            createdPod = resource.waitUntilReady(5, TimeUnit.MINUTES);
            String podIp = createdPod.getStatus().getPodIP();
            System.out.println(podIp + ":" + port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
