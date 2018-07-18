package be.looorent.jflu.entity;

import be.looorent.jflu.EventData;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * When an event represents a CRUD operation on an entity, it can use this class as {@link EventData} implementation.
 * @author Lorent Lempereur {@literal <lorent.lempereur.dev@gmail.com>}
 */
public class EntityData implements EventData {

    @JsonDeserialize(using = EventSerializer.IdDeserializer.class)
    private final Object id;
    private final UUID requestId;
    private final String entityName;
    private final EntityActionName actionName;
    private final Map<String, Object> userMetadata;
    private final Map<String, Long> associations;
    private final Map<String, List<Object>> changes;

    @JsonCreator
    public EntityData(@JsonProperty("entityId")     Object id,
                      @JsonProperty("requestId")    UUID requestId,
                      @JsonProperty("entityName")   String entityName,
                      @JsonProperty("actionName")   EntityActionName actionName,
                      @JsonProperty("userMetadata") Map<String, Object> userMetadata,
                      @JsonProperty("associations") Map<String, Long> associations,
                      @JsonProperty("changes")      Map<String, List<Object>> changes) {
        this.id = id;
        this.requestId = requestId;
        this.entityName = entityName;
        this.actionName = actionName;
        this.userMetadata = userMetadata;
        this.associations = associations;
        this.changes = changes;
    }

    public Object getId() {
        return id;
    }

    public UUID getRequestId() {
        return requestId;
    }

    public String getEntityName() {
        return entityName;
    }

    public EntityActionName getActionName() {
        return actionName;
    }

    public Map<String, Object> getUserMetadata() {
        return userMetadata;
    }

    public Map<String, Long> getAssociations() {
        return associations;
    }

    public Map<String, List<Object>> getChanges() {
        return changes;
    }
}
