package be.looorent.jflu;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
public class EventMetadata {

    private final UUID id;
    private final String name;
    private final String eventEmitter;
    private final LocalDateTime timestamp;
    private final EventKind kind;
    private final EventStatus status;

    public EventMetadata(UUID id,
                         String name,
                         String eventEmitter,
                         LocalDateTime timestamp,
                         EventKind kind,
                         EventStatus status) {
        this.id = id;
        this.name = name;
        this.eventEmitter = eventEmitter;
        this.timestamp = timestamp;
        this.kind = kind;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEventEmitter() {
        return eventEmitter;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public EventKind getKind() {
        return kind;
    }

    public EventStatus getStatus() {
        return status;
    }
}