package be.looorent.jflu.subscriber;

import be.looorent.jflu.EventKind;

/**
 * Mirror enumeration of {@link EventKind} for matching events.
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
public enum EventMappingKind {

    ENTITY_CHANGE,
    REQUEST,
    ALL;

    public static EventMappingKind valueOf(EventKind kind) {
        return valueOf(kind.name());
    }
}
