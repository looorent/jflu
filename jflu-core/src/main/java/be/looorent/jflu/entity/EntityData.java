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

import static java.util.Collections.unmodifiableMap;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;

/**
 * When an event represents a CRUD operation on an entity, it can use this class as {@link EventData} implementation.
 * @author Lorent Lempereur {@literal <lorent.lempereur.dev@gmail.com>}
 */
public class EntityData implements EventData {

    private final Object id;
    private final UUID requestId;
    private final String entityName;
    private final EntityActionName actionName;
    private final Map<String, Payload> userMetadata;
    private final Map<String, Payload> requestMetadata;

    private final Map<String, Object> associations;

    private final Map<String, EntityChange> changes;

    /**
     * View of associations where all keys ends with `_id` or `Id`
     */
    private final Map<String, Long> associationIds;

    /**
     * View of associations where all keys ends with `_type` or `Type`
     */
    private final Map<String, String> associationTypes;

    @JsonCreator
    public EntityData(@JsonDeserialize(using = EventSerializer.IdDeserializer.class) @JsonProperty("entityId")        Object id,
                      @JsonProperty("requestId")       UUID requestId,
                      @JsonProperty("entityName")      String entityName,
                      @JsonProperty("actionName")      EntityActionName actionName,
                      @JsonProperty("userMetadata")    Map<String, Payload> userMetadata,
                      @JsonProperty("requestMetadata") Map<String, Payload> requestMetadata,
                      @JsonProperty("associations")    Map<String, Object> associations,
                      @JsonProperty("changes")         Map<String, EntityChange> changes) {
        this.id = id;
        this.requestId = requestId;
        this.entityName = entityName;
        this.actionName = actionName;
        this.userMetadata = ofNullable(userMetadata).orElseGet(Collections::emptyMap);
        this.requestMetadata = ofNullable(requestMetadata).orElseGet(Collections::emptyMap);
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

    public Map<String, Payload> getRequestMetadata() {
        return requestMetadata;
    }

    public <T> Optional<T> getUserMetadataFor(String name, Class<T> clazz) {
        return ofNullable(userMetadata.get(name))
                .flatMap(metadata -> metadata.get(clazz));
    }

    public Map<String, Object> getAssociations() {
        return unmodifiableMap(associations);
    }

    @JsonIgnore
    public Map<String, Long> getAssociationIds() {
        return unmodifiableMap(associationIds);
    }

    public Long getAssociationId(String id) {
        return associationIds.get(id);
    }

    /**
     * @param id the association's id
     * @return true iff there is an association with this id, even when the value is null; false otherwise
     */
    public boolean hasAssociationId(String id) {
        return associationIds.containsKey(id);
    }

    @JsonIgnore
    public Map<String, String> getAssociationTypes() {
        return unmodifiableMap(associationTypes);
    }

    public String getAssociationType(String typeId) {
        return associationTypes.get(typeId);
    }

    /**
     * @param typeId the association's typeId
     * @return true iff there is an association with this typeId, even when the value is null; false otherwise
     */
    public boolean hasAssociationType(String typeId) {
        return associationTypes.containsKey(typeId);
    }

    public Map<String, EntityChange> getChanges() {
        return unmodifiableMap(changes);
    }

    /**
     * @param name the name of the field that has changed
     * @return true iff the changes contain an entry with the provided name, even if the value is null; false otherwise
     */
    public boolean hasChange(String name) {
        return changes.containsKey(name);
    }

    public <T> Optional<T> afterValueOf(String name, Class<T> clazz) {
        return ofNullable(changes.get(name))
                .flatMap(change -> change.afterValue(clazz));
    }

    public <T> Optional<T> beforeValueOf(String name, Class<T> clazz) {
        return ofNullable(changes.get(name))
                .flatMap(change -> change.beforeValue(clazz));
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
