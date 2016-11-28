package be.looorent.jflu.subscriber;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
public enum EventMappingKind {

    ENTITY_CHANGED,
    REQUEST,
    ALL;

}
