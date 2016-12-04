package be.looorent.jflu;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
public enum EventStatus {

    @JsonProperty("new") NEW,
    @JsonProperty("replayed") REPLAYED
}
