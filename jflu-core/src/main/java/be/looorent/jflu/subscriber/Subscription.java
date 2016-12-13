package be.looorent.jflu.subscriber;

import be.looorent.jflu.Event;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

import static java.util.UUID.randomUUID;

/**
 * Immutable class that represents a consumer of events mapped to all events
 * that satisfy a {@link SubscriptionQuery}.
 * @author Lorent Lempereur {@literal <lorent.lempereur.dev@gmail.com>}
 */
public class Subscription {

    private final UUID id;
    private final String name;
    private final SubscriptionQuery query;
    private final Consumer<Event> projector;

    public Subscription(SubscriptionQuery query,
                        String name,
                        Consumer<Event> projector) {
        this.id = randomUUID();
        this.query = query;
        this.name = name;
        this.projector = projector;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof Subscription)) {
            return false;
        }

        Subscription otherSubscription = (Subscription) other;
        return Objects.equals(id, otherSubscription.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public UUID getId() {
        return id;
    }

    public SubscriptionQuery getQuery() {
        return query;
    }

    public Consumer<Event> getProjector() {
        return projector;
    }

    public String getName() {
        return name;
    }
}
