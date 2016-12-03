package be.looorent.jflu;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
public class Event {

    private final EventMetadata metadata;
    private final EventData data;

    @JsonCreator
    public Event(@JsonProperty("metadata") EventMetadata metadata,
                 @JsonProperty("data") EventData data) {
        this.metadata = metadata;
        this.data = data;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof Event)) {
            return false;
        }

        Event otherEvent = (Event) other;
        return Objects.equals(metadata, otherEvent.getMetadata());
    }

    @Override
    public int hashCode() {
        return Objects.hash(metadata);
    }

    public EventMetadata getMetadata() {
        return metadata;
    }

    public EventData getData() {
        return data;
    }

    public UUID getId() {
        return metadata.getId();
    }


    public String getName() {
        return metadata.getName();
    }

    public String getEventEmitter() {
        return metadata.getEmitter();
    }

    public LocalDateTime getTimestamp() {
        return metadata.getTimestamp();
    }

    public EventKind getKind() {
        return metadata.getKind();
    }

    public EventStatus getStatus() {
        return metadata.getStatus();
    }
}
