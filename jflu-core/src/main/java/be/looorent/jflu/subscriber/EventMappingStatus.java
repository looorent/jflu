package be.looorent.jflu.subscriber;

import be.looorent.jflu.EventStatus;

/**
 * Mirror enumeration of {@link EventStatus} for matching events.
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
public enum EventMappingStatus {

    NEW,
    REPLAYED,
    ALL;

    public static EventMappingStatus valueOf(EventStatus status) {
        return valueOf(status.name());
    }
}
