package be.looorent.jflu;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
public class EventMetadata {

    private final UUID id;
    private final String name;
    private final String emitter;
    private final LocalDateTime timestamp;
    private final EventKind kind;
    private final EventStatus status;

    @JsonCreator
    public EventMetadata(@JsonProperty("id") UUID id,
                         @JsonProperty("name") String name,
                         @JsonProperty("emitter") String emitter,
                         @JsonProperty("timestamp") LocalDateTime timestamp,
                         @JsonProperty("kind") EventKind kind,
                         @JsonProperty("status") EventStatus status) {
        this.id = id;
        this.name = name;
        this.emitter = emitter;
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

    public String getEmitter() {
        return emitter;
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