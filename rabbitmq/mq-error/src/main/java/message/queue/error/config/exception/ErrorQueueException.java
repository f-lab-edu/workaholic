package message.queue.error.config.exception;

import lombok.Getter;
import message.queue.error.config.ExceptionStatus;

import java.util.UUID;

@Getter
public class ErrorQueueException extends Exception {
    private UUID transactionId;
    private ExceptionStatus status;

    public ErrorQueueException() {

    }

    public ErrorQueueException(UUID transactionId, ExceptionStatus status) {
        this.transactionId = transactionId;
        this.status = status;
    }

    public ErrorQueueException(String message, UUID transactionId, ExceptionStatus status) {
        super(message);
        this.transactionId = transactionId;
        this.status = status;
    }
}
