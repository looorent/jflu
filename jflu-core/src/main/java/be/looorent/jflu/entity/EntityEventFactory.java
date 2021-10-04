package be.looorent.jflu.entity;

import be.looorent.jflu.Configuration;
import be.looorent.jflu.Event;
import be.looorent.jflu.EventMetadata;
import be.looorent.jflu.Payload;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static be.looorent.jflu.EventKind.ENTITY_CHANGE;
import static be.looorent.jflu.EventStatus.NEW;
import static be.looorent.jflu.entity.EntityActionName.*;
import static java.time.LocalDateTime.now;
import static java.time.ZoneOffset.UTC;
import static java.util.Arrays.asList;

/**
 * Create events for each create/delete/update operation on an entity.
 * @author Lorent Lempereur {@literal <lorent.lempereur.dev@gmail.com>}
 */
public class EntityEventFactory {

    private final String emitter;

    public EntityEventFactory(String emitter) {
        this.emitter = emitter;
    }

    public EntityEventFactory() {
        this(Configuration.getInstance().getEmitter());
    }

    public Event createEventOnSave(Class<?> entityType,
                                   Serializable entityId,
                                   Map<String, Object> stateByPropertyName,
                                   UUID sessionId) {
        EventMetadata metadata = createMetadata(entityType, CREATE, sessionId);

        Map<String, EntityChange> changes = new HashMap<>();
        for (Map.Entry<String, Object> stateAndPropertyName : stateByPropertyName.entrySet()) {
            changes.put(stateAndPropertyName.getKey(), new EntityChange(asList(null, new Payload(stateAndPropertyName.getValue()))));
        }
        return new Event(metadata, new EntityData(entityId,
                null,
                entityType.getSimpleName(),
                CREATE,
                null,
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
                null,
                new HashMap<>()));
    }

    public Event createEventOnUpdate(Class<?> entityType,
                                     Serializable entityId,
                                     Map<String, List<Object>> changeByPropertyName,
                                     UUID sessionId) {
        EventMetadata metadata = createMetadata(entityType, UPDATE, sessionId);
        Map<String, EntityChange> changes = new HashMap<>();
        for (Map.Entry<String, List<Object>> changeAndPropertyName : changeByPropertyName.entrySet()) {
            List<Payload> payloads = changeAndPropertyName.getValue().stream().map(Payload::new).collect(Collectors.toList());
            changes.put(changeAndPropertyName.getKey(), new EntityChange(payloads));
        }

        return new Event(metadata, new EntityData(entityId,
                null,
                entityType.getSimpleName(),
                CREATE,
                null,
                null,
                null,
                changes));
    }

    private EventMetadata createMetadata(Class<?> entityType,
                                         EntityActionName actionName,
                                         UUID sessionId) {
        return new EventMetadata(sessionId,
                actionName.name().toLowerCase() + " " + entityType.getSimpleName(),
                emitter,
                now(UTC),
                ENTITY_CHANGE,
                NEW);
    }
}
