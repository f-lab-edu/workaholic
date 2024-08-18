package datasource.transaction.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "EVENT_TRACE")
public class EventTrace {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TRACE_ID")
    private Long id;

    @Column(name = "TRANSACTION_ID")
    private UUID transactionId;

    @Column(name = "TYPE")
    private String exceptionType;

    @Column(name = "APPLICATION_NAME")
    private String applicationName;

    @Column(name = "MESSAGE", nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "TRACE_MESSAGE", columnDefinition = "TEXT")
    private String stacktrace;

    @Column(name = "TIMESTAMP")
    private LocalDateTime time;

    public EventTrace(UUID transactionId, String exceptionType, String applicationName, String message, String stacktrace) {
        this.transactionId = transactionId;
        this.exceptionType = exceptionType;
        this.applicationName = applicationName;
        this.message = message;
        this.stacktrace = stacktrace;
        this.time = LocalDateTime.now();
    }
}
