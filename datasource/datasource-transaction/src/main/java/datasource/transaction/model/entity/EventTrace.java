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
    private String type;

    @Column(name = "MESSAGE", nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "TIMESTAMP")
    private LocalDateTime time;

    public EventTrace(UUID transactionId, String type, String message) {
        this.transactionId = transactionId;
        this.type = type;
        this.message = message;
    }

    public EventTrace(UUID transactionId, String message) {
        this.transactionId = transactionId;
        this.type = "EVENT";
        this.message = message;
    }
}
