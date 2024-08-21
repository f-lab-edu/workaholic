package message.queue.kubernetes.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "rabbitmq.kubernetes")
public class KubernetesMessageQueueProperties {
    private final String host;
    private final int port;
    private final String username;
    private final String password;
    private final String exchange;

    public KubernetesMessageQueueProperties(String host, int port, String username, String password, String exchange) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.exchange = exchange;
    }
}
