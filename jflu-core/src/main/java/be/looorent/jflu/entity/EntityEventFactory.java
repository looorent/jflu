package be.looorent.jflu.entity;

import be.looorent.jflu.Configuration;
import be.looorent.jflu.Event;
import be.looorent.jflu.EventMetadata;

import java.util.UUID;

import static be.looorent.jflu.EventKind.ENTITY_CHANGED;
import static be.looorent.jflu.EventStatus.NEW;
import static be.looorent.jflu.entity.EntityActionName.CREATE;
import static be.looorent.jflu.entity.EntityActionName.DESTROY;
import static be.looorent.jflu.entity.EntityActionName.UPDATE;
import static java.time.LocalDateTime.now;

/**
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
public class EntityEventFactory {

    public Event createEventOnSave(Object entity) {
        EventMetadata metadata = createMetadata(entity, CREATE);
        return new Event(metadata, new EntityData());
    }

    public Event createEventOnDelete(Object entity) {
        EventMetadata metadata = createMetadata(entity, DESTROY);
        return new Event(metadata, new EntityData());
    }

    public Event createEventOnUpdate(Object entity) {
        EventMetadata metadata = createMetadata(entity, UPDATE);
        return new Event(metadata, new EntityData());
    }

    private EventMetadata createMetadata(Object entity,
                                         EntityActionName actionName) {
        String typeName = entity.getClass().getSimpleName();
        return new EventMetadata(UUID.randomUUID(),
                actionName.name().toLowerCase() + " " + typeName,
                Configuration.getInstance().getEmitter(),
                now(),
                ENTITY_CHANGED,
                NEW);
    }
}
