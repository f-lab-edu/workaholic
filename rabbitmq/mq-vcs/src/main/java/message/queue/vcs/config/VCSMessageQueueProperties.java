package message.queue.vcs.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "rabbitmq.vcs")
public class VCSMessageQueueProperties {
    private final String host;
    private final int port;
    private final String username;
    private final String password;
    private final String exchange;

    public VCSMessageQueueProperties(String host, int port, String username, String password, String exchange) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.exchange = exchange;
    }
}
