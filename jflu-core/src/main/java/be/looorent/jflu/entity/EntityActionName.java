package be.looorent.jflu.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * CRUD operation type that has been applied on an entity.
 * @author Lorent Lempereur {@literal <lorent.lempereur.dev@gmail.com>}
 */
public enum EntityActionName {

    @JsonProperty("create") CREATE,
    @JsonProperty("update") UPDATE,
    @JsonProperty("destroy") DESTROY

}
