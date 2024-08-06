package com.project.config.rabbit;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "spring.rabbitmq")
public class RabbitMessageQueueProperties {
    private final String host;
    private final int port;
    private final String username;
    private final String password;

    public RabbitMessageQueueProperties(String host, int port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }
}
