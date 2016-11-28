package be.looorent.jflu.subscriber;

import be.looorent.jflu.Event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
public class SubscriptionQuery {

    private final String emitter;
    private final EventMappingKind kind;
    private final String name;
    private final EventMappingStatus status;

    public SubscriptionQuery(EventMapping mapping) {
        this.emitter = mapping.emitter();
        this.kind = mapping.kind();
        this.name = mapping.name();
        this.status = mapping.status();
    }

    public SubscriptionQuery(String emitter,
                             EventMappingKind kind,
                             String name,
                             EventMappingStatus status) {
        this.emitter = emitter;
        this.kind = kind;
        this.name = name;
        this.status = status;
    }

    public static final SubscriptionQuery exactMatchWith(Event event) {
        return new SubscriptionQuery(event.getEventEmitter(),
                EventMappingKind.valueOf(event.getKind().name()),
                event.getName(),
                EventMappingStatus.valueOf(event.getStatus().name()));
    }

    public String getEmitter() {
        return emitter;
    }

    public EventMappingKind getKind() {
        return kind;
    }

    public String getName() {
        return name;
    }

    public EventMappingStatus getStatus() {
        return status;
    }

    public boolean matchesAllStatuses() {
        return status == EventMappingStatus.ALL;
    }

    public boolean matchesAllKinds() {
        return kind == EventMappingKind.ALL;
    }

    public boolean matchesAllEmitters() {
        return emitter.isEmpty();
    }

    public boolean matchesAllNames() {
        return name.isEmpty();
    }

    public SubscriptionQuery forAllKinds() {
        return new SubscriptionQuery(emitter,
                EventMappingKind.ALL,
                name,
                status);
    }

    public SubscriptionQuery forAllEmitters() {
        return new SubscriptionQuery("",
                kind,
                name,
                status);
    }

    public SubscriptionQuery forAllNames() {
        return new SubscriptionQuery(emitter,
                kind,
                "",
                status);
    }

    public SubscriptionQuery forAllStatuses() {
        return new SubscriptionQuery(emitter,
                kind,
                name,
                EventMappingStatus.ALL);
    }

    public static final Collection<SubscriptionQuery> allPossibilitiesFor(Event event) {
        List<EventMappingStatus> possibleStatuses = newArrayList(EventMappingStatus.ALL, EventMappingStatus.valueOf(event.getStatus().name()));
        List<String> possibleEmitters = newArrayList("", event.getEventEmitter());
        List<EventMappingKind> possibleKinds = newArrayList(EventMappingKind.ALL, EventMappingKind.valueOf(event.getKind().name()));
        List<String> possibleNames = newArrayList("", event.getName());

        List<SubscriptionQuery> queries = new ArrayList<>();
        for (EventMappingStatus status : possibleStatuses) {
            for (String emitter : possibleEmitters) {
                for (EventMappingKind kind : possibleKinds) {
                    for (String name : possibleNames) {
                        SubscriptionQuery query = new SubscriptionQuery(emitter, kind, name, status);
                        queries.add(query);
                    }
                }
            }
        }
        return queries;
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