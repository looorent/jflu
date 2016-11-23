package be.looorent.jflu.subscriber;

import be.looorent.jflu.EventStatus;

import java.util.Objects;

/**
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
public class SubscriptionQuery {

    private final String emitter;
    private final String kind;
    private final String name;
    private final EventStatus status;

    public SubscriptionQuery(String emitter,
                             String kind,
                             String name,
                             EventStatus status) {
        this.emitter = emitter;
        this.kind = kind;
        this.name = name;
        this.status = status;
    }

    public String getEmitter() {
        return emitter;
    }

    public String getKind() {
        return kind;
    }

    public String getName() {
        return name;
    }

    public EventStatus getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof SubscriptionQuery)) {
            return false;
        }

        SubscriptionQuery otherQuery = (SubscriptionQuery) other;
        return Objects.equals(emitter, otherQuery.emitter) &&
               Objects.equals(kind, otherQuery.kind) &&
               Objects.equals(name, otherQuery.name) &&
               status == otherQuery.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(emitter, kind, name, status);
    }
}