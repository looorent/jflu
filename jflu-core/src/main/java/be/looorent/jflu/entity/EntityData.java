package be.looorent.jflu.entity;

import be.looorent.jflu.EventData;
import be.looorent.jflu.Payload;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;

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
    private final Map<String, Payload> userMetadata;

    private final Map<String, Object> associations;

    private final Map<String, EntityChange> changes;

    /**
     * View of associations where all keys ends with `_id` or `Id`
     */
    @JsonIgnore
    private final Map<String, Long> associationIds;

    /**
     * View of associations where all keys ends with `_type` or `Type`
     */
    @JsonIgnore
    private final Map<String, String> associationTypes;

    @JsonCreator
    public EntityData(@JsonProperty("entityId")     Object id,
                      @JsonProperty("requestId")    UUID requestId,
                      @JsonProperty("entityName")   String entityName,
                      @JsonProperty("actionName")   EntityActionName actionName,
                      @JsonProperty("userMetadata") Map<String, Payload> userMetadata,
                      @JsonProperty("associations") Map<String, Object> associations,
                      @JsonProperty("changes")      Map<String, EntityChange> changes) {
        this.id = id;
        this.requestId = requestId;
        this.entityName = entityName;
        this.actionName = actionName;
        this.userMetadata = ofNullable(userMetadata).orElseGet(Collections::emptyMap);
        this.associations = ofNullable(associations).orElseGet(Collections::emptyMap);
        this.changes = changes;
        this.associationIds = Association.reduce(this.associations, Association::isId, Association::castAssociationId);
        this.associationTypes = Association.reduce(this.associations, Association::isType, Association::castAssociationType);
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

    public Map<String, Payload> getUserMetadata() {
        return userMetadata;
    }

    public Map<String, Object> getAssociations() {
        return Collections.unmodifiableMap(associations);
    }

    public Map<String, Long> getAssociationIds() {
        return Collections.unmodifiableMap(associationIds);
    }

    public Map<String, String> getAssociationTypes() {
        return Collections.unmodifiableMap(associationTypes);
    }

    public Map<String, EntityChange> getChanges() {
        return changes;
    }

    private static class Association {
        private static Long castAssociationId(Object valueToCast) {
            return ofNullable(valueToCast)
                    .filter(value -> value instanceof Number)
                    .map(Number.class::cast)
                    .map(Number::longValue)
                    .orElse(null);
        }

        private static String castAssociationType(Object valueToCast) {
            return ofNullable(valueToCast)
                    .map(String::valueOf)
                    .orElse(null);
        }

        private static boolean isId(String key) {
            return key.endsWith("Id") || key.endsWith("_id");
        }

        private static boolean isType(String key) {
            return key.endsWith("Type") || key.endsWith("_type");
        }

        private static <T> Map<String, T> reduce(Map<String, Object> associations,
                                                 Predicate<String> keyFilter,
                                                 Function<Object, T> transformer) {
            return associations.entrySet()
                    .stream()
                    .filter(entry -> keyFilter.test(entry.getKey()))
                    .collect(HashMap::new, (map, entry) -> map.put(entry.getKey(), transformer.apply(entry.getValue())), Map::putAll);
        }
    }
}
