package kubernetes.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kubernetes.build.model.ProjectBuild;
import kubernetes.build.service.ProjectBuildService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class MessageQueueListener {
    private final ObjectMapper objectMapper;
    private final ProjectBuildService buildService;

    public MessageQueueListener(ObjectMapper objectMapper, ProjectBuildService buildService) {
        this.objectMapper = objectMapper;
        this.buildService = buildService;
    }

    @RabbitListener(queues = "workaholic.kubernetes", concurrency = "2")
    public void onMessage(Message message) throws IOException {
        String data = objectMapper.readValue(message.getBody(), String.class);
        log.info("data : " + data );
    }
}
