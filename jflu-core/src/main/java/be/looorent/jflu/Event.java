package be.looorent.jflu;

import java.util.Objects;

/**
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
public class Event {

    private final EventMetadata metadata;
    private final EventData data;

    public Event(EventMetadata metadata, EventData data) {
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
}
