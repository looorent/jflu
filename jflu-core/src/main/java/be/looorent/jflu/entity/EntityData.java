package be.looorent.jflu.entity;

import be.looorent.jflu.EventData;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
public class EntityData implements EventData {

    private final Serializable id;
    private final String entityName;
    private final EntityActionName actionName;
    private final Object userMetadata;
    private final Object associations;

    public EntityData() {
        id = null;
        entityName = null;
        actionName = null;
        userMetadata = null;
        associations = null;
    }

    @JsonCreator
    public EntityData(@JsonProperty("id")           Serializable id,
                      @JsonProperty("entityName")   String entityName,
                      @JsonProperty("actionName")   EntityActionName actionName,
                      @JsonProperty("userMetadata") Object userMetadata,
                      @JsonProperty("associations") Object associations) {
        this.id = id;
        this.entityName = entityName;
        this.actionName = actionName;
        this.userMetadata = userMetadata;
        this.associations = associations;
    }

    public Serializable getId() {
        return id;
    }

    public String getEntityName() {
        return entityName;
    }

    public EntityActionName getActionName() {
        return actionName;
    }

    public Object getUserMetadata() {
        return userMetadata;
    }

    public Object getAssociations() {
        return associations;
    }
}
