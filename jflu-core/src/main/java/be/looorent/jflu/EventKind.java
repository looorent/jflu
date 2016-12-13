package be.looorent.jflu;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Kind of event.
 * @author Lorent Lempereur {@literal <lorent.lempereur.dev@gmail.com>}
 */
public enum EventKind {

    @JsonProperty("entity_change") ENTITY_CHANGE,
    @JsonProperty("request") REQUEST

}
