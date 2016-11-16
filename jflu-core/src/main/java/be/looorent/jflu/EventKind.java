package be.looorent.jflu;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
public enum EventKind {

    @JsonProperty("entity_changed") ENTITY_CHANGED,
    @JsonProperty("request") REQUEST

}
