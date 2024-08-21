package message.queue.error.config.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public abstract class ErrorQueueException extends Exception {
    private final UUID transactionId;
    private final String applicationName;

    public ErrorQueueException(UUID transactionId, String applicationName, String message) {
        super(message);
        this.transactionId = transactionId;
        this.applicationName = applicationName;
    }
}
