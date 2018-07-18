package be.looorent.jflu.entity;

import be.looorent.jflu.Configuration;
import be.looorent.jflu.Event;
import be.looorent.jflu.EventMetadata;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static be.looorent.jflu.EventKind.ENTITY_CHANGE;
import static be.looorent.jflu.EventStatus.NEW;
import static be.looorent.jflu.entity.EntityActionName.*;
import static com.google.common.collect.Lists.newArrayList;
import static java.time.LocalDateTime.now;

/**
 * Create events for each create/delete/update operation on an entity.
 * @author Lorent Lempereur {@literal <lorent.lempereur.dev@gmail.com>}
 */
public class EntityEventFactory {

    public Event createEventOnSave(Class<?> entityType,
                                   Serializable entityId,
                                   Map<String, Object> stateByPropertyName,
                                   UUID sessionId) {
        EventMetadata metadata = createMetadata(entityType, CREATE, sessionId);

        Map<String, List<Object>> changes = new HashMap<>();
        for (Map.Entry<String, Object> stateAndPropertyName : stateByPropertyName.entrySet()) {
            changes.put(stateAndPropertyName.getKey(), newArrayList(null, stateAndPropertyName.getValue()));
        }
        return new Event(metadata, new EntityData(entityId,
                null,
                entityType.getSimpleName(),
                CREATE,
                null,
                null,
                changes));
    }

    public Event createEventOnDelete(Class<?> entityType,
                                     Serializable entityId,
                                     UUID sessionId) {
        EventMetadata metadata = createMetadata(entityType, DESTROY, sessionId);
        return new Event(metadata, new EntityData(entityId,
                null,
                entityType.getSimpleName(),
                DESTROY,
                null,
                null,
                new HashMap<>()));
    }

    public Event createEventOnUpdate(Class<?> entityType,
                                     Serializable entityId,
                                     Map<String, List<Object>> changeByPropertyName,
                                     UUID sessionId) {
        EventMetadata metadata = createMetadata(entityType, UPDATE, sessionId);
        return new Event(metadata, new EntityData(entityId,
                null,
                entityType.getSimpleName(),
                CREATE,
                null,
                null,
                changeByPropertyName));
    }

    private EventMetadata createMetadata(Class<?> entityType,
                                         EntityActionName actionName,
                                         UUID sessionId) {
        return new EventMetadata(sessionId,
                actionName.name().toLowerCase() + " " + entityType.getSimpleName(),
                Configuration.getInstance().getEmitter(),
                now(),
                ENTITY_CHANGE,
                NEW);
    }
}
