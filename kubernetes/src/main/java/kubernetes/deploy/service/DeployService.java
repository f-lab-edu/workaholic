package kubernetes.deploy.service;

import io.fabric8.kubernetes.api.model.IntOrString;
import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.NamespaceBuilder;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodBuilder;
import io.fabric8.kubernetes.api.model.ServiceBuilder;
import io.fabric8.kubernetes.api.model.ServicePort;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class DeployService {
    private final KubernetesClient kubernetesClient;

    public DeployService(KubernetesClient kubernetesClient) {
        this.kubernetesClient = kubernetesClient;
    }

    private void createNamespace(String name) throws Exception {
        try {
            Namespace namespace = kubernetesClient.namespaces().withName(name).get();
            if( namespace == null ) {
                log.info("Namespace {} Creating...", name);
                namespace = kubernetesClient.namespaces()
                        .resource(
                                new NamespaceBuilder()
                                        .withNewMetadata()
                                            .withName(name)
                                        .endMetadata().build()).create();
                log.info("Namespace {} created.", name);
                kubernetesClient.namespaces()
                        .withName(namespace.getMetadata().getName())
                        .waitUntilCondition(n -> n != null && n.getStatus() != null, 5, TimeUnit.MINUTES);
                log.info("Namespace {} is ready.", name);
            } else {
                log.info("Namespace {} already exists.", name);
            }
        } catch (Exception e) {
            log.error("Error creating or waiting for Namespace: {}", e.getMessage());
            throw e;
        }
    }

    private void createPod(String namespace, String podName, String imageName, int accessPort) throws Exception {
        try {
            Pod pod = kubernetesClient.pods()
                    .inNamespace(namespace).withName(podName).get();
            if( pod == null ) {
                log.info("Pod {} at {} Creating...", podName, namespace);
                pod = kubernetesClient.pods().inNamespace(namespace)
                        .resource(new PodBuilder()
                                .withNewMetadata()
                                    .withName(podName)
                                    .withNamespace(namespace)
                                .endMetadata()
                                .withNewSpec()
                                    .addNewContainer()
                                        .withName(podName + "-container")
                                        .withImage(imageName)
                                        .addNewPort()
                                            .withContainerPort(accessPort) // Container's internal port
                                        .endPort()
                                    .endContainer()
                                .endSpec().build()).create();
                log.info("Pod {} created.", podName);
                kubernetesClient.pods()
                        .inNamespace(namespace)
                        .resource(pod)
                        .waitUntilReady(5, TimeUnit.MINUTES);
                log.info("Pod {} is ready with IP {}:{}", podName, pod.getStatus().getPodIP(), accessPort);
            } else {
                log.info("Pod {} already exists.", podName);
            }
        } catch (Exception e) {
            log.error("Error creating or waiting for Pod: {}", e.getMessage());
            throw e;
        }
    }

    private String getServiceUrl(io.fabric8.kubernetes.api.model.Service service) {
        // NodePort와 Node IP 가져오기
        List<ServicePort> ports = service.getSpec().getPorts();
        if (ports.isEmpty()) {
            throw new KubernetesClientException("No ports found in the Service");
        }
        int nodePort = ports.get(0).getNodePort();

        // 노드 IP 가져오기 (가정: 첫 번째 노드를 사용)
        String nodeIp = kubernetesClient.getConfiguration().getMasterUrl();

        // 외부에서 접근 가능한 URL 생성
        String url = "http://" + nodeIp + ":" + nodePort;
        System.out.println("Application is available at: " + url);

        return url;
    }

    public io.fabric8.kubernetes.api.model.Service createService(String namespace, String podName, int servicePort, int targetPort) throws Exception {
        try {
            io.fabric8.kubernetes.api.model.Service service = kubernetesClient.services()
                    .inNamespace(namespace).withName(podName + "-existingService").get();
            if( service != null )
                return service;

            service = kubernetesClient.services()
                    .inNamespace(namespace)
                    .resource(new ServiceBuilder()
                            .withNewMetadata()
                                .withName(podName + "-service")
                                .withNamespace(namespace)
                            .endMetadata()
                            .withNewSpec()
                                .addNewPort()
                                    .withPort(servicePort)
                                    .withTargetPort(new IntOrString(targetPort))
                                    .withNodePort(servicePort) // NodePort 설정
                                .endPort()
                                .withSelector(Collections.singletonMap("app", podName))
                                .withType("NodePort") // NodePort 타입 설정
                            .endSpec()
                            .build()).create();
            log.info("Service {} created...", service.getMetadata().getName());
            return service;
        } catch (Exception e) {
            log.error("Error creating or waiting for Service : {}", e.getMessage());
            throw e;
        }
    }

    public void deployApplication(String namespace, String imageName, int port) {
        try {
            createNamespace(namespace);
            createPod(namespace, "test-pod2", imageName, port);
            io.fabric8.kubernetes.api.model.Service service = createService(namespace, "test-pod2", 30080, port);
            String path = getServiceUrl(service);
            log.info("Address : {} ", path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
