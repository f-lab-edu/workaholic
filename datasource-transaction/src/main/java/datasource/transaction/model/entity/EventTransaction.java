package datasource.transaction.model.entity;

import datasource.transaction.model.enumeration.EventStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Entity
@Table(name = "EVENT_TRANSACTION")
public class EventTransaction {
    @Id
    @GeneratedValue(generator = "uuid2")
    private UUID id;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private EventStatus status;

    public EventTransaction() {
        this.id = UUID.randomUUID();
        this.status = EventStatus.START;
    }
}
