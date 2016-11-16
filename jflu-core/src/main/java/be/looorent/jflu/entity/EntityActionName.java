package be.looorent.jflu.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
public enum EntityActionName {

    @JsonProperty("create") CREATE,
    @JsonProperty("update") UPDATE,
    @JsonProperty("destroy") DESTROY

}
