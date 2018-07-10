package be.looorent.jflu.entity;

import be.looorent.jflu.EventData;
import be.looorent.jflu.EventKind;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;
import java.util.Map;

/**
 * When an event represents a CRUD operation on an entity, it can use this class as {@link EventData} implementation.
 * @author Lorent Lempereur {@literal <lorent.lempereur.dev@gmail.com>}
 */
public class EntityData implements EventData {

    @JsonDeserialize(using = EventSerializer.IdDeserializer.class)
    private final Object id;
    private final String entityName;
    private final EntityActionName actionName;
    private final Object userMetadata;
    private final Map<String, List<Long>> associations;
    private final Map<String, List<Object>> changes;

    @JsonCreator
    public EntityData(@JsonProperty("id")           Object id,
                      @JsonProperty("entityName")   String entityName,
                      @JsonProperty("actionName")   EntityActionName actionName,
                      @JsonProperty("userMetadata") Object userMetadata,
                      @JsonProperty("associations") Map<String, List<Long>> associations,
                      @JsonProperty("changes")      Map<String, List<Object>> changes) {
        this.id = id;
        this.entityName = entityName;
        this.actionName = actionName;
        this.userMetadata = userMetadata;
        this.associations = associations;
        this.changes = changes;
    }

    public Object getId() {
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

    public Map<String, List<Long>> getAssociations() {
        return associations;
    }

    public Map<String, List<Object>> getChanges() {
        return changes;
    }
}
