package kubernetes.deploy.service;

import io.fabric8.kubernetes.api.model.IntOrString;
import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.NamespaceBuilder;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodBuilder;
import io.fabric8.kubernetes.api.model.ServiceBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
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
            throw new Exception();
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
                                    .addToLabels(Collections.singletonMap("app", podName))
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
            throw new Exception();
        }
    }

    private String getAccessAddress(io.fabric8.kubernetes.api.model.Service service) {
        int port = service.getSpec().getPorts().getFirst().getNodePort();
        String host = kubernetesClient.getConfiguration().getMasterUrl().replaceFirst("https?://", "").split(":")[0];

        String ipAddress;
        try {
            InetAddress inetAddress = InetAddress.getByName(host);
            ipAddress = inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            log.error("Failed to resolve host to IP : {}", e.getMessage());
            ipAddress = host;
        }

        return ipAddress + ":" + port;
    }

    public io.fabric8.kubernetes.api.model.Service createService(String namespace, String podName, int servicePort, int targetPort) throws Exception {
        try {
            io.fabric8.kubernetes.api.model.Service service = kubernetesClient.services()
                    .inNamespace(namespace).withName(podName + "-service").get();
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

    public void deployApplication(String namespace, String podName, String imageName, int servicePort, int targetPort) {
        try {
            createNamespace(namespace);
            createPod(namespace, podName, imageName, targetPort);
            io.fabric8.kubernetes.api.model.Service service = createService(namespace, podName, servicePort, targetPort);
            String path = getAccessAddress(service);
            log.info("Address : {} ", path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
