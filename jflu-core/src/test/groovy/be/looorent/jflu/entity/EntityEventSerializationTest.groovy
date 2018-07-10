package be.looorent.jflu.entity

import be.looorent.jflu.Configuration
import be.looorent.jflu.Event
import be.looorent.jflu.EventData
import be.looorent.jflu.EventMetadata
import com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Specification

import static be.looorent.jflu.EventKind.ENTITY_CHANGE
import static be.looorent.jflu.EventStatus.NEW
import static be.looorent.jflu.entity.EntityActionName.CREATE
import static java.time.LocalDateTime.of
import static java.time.Month.APRIL

/**
 * @author Lorent Lempereur {@literal <lorent.lempereur.dev@gmail.com>}
 */
class EntityEventSerializationTest extends Specification {

    def "parsing an event from a JSON file works"() {
        when: "an event is parsed from a JSON file"
        ObjectMapper jsonMapper = Configuration.instance.defaultJsonMapper
        Event event = this.class.getResource("entity_event.json").withInputStream { inputStream ->
            jsonMapper.readValue inputStream, Event
        }

        then: "this event has correct properties"
        EventMetadata metadata = event.metadata
        EventData data = event.data

        metadata.id.toString() == "0d5ce258-1314-45d9-8ace-ce43d42255cc"
        metadata.name == "create animal"
        metadata.emitter == "pet-store"
        metadata.timestamp == of(2016, APRIL, 23, 18, 25, 43)
        metadata.kind == ENTITY_CHANGE
        metadata.status == NEW

        data instanceof EntityData
        EntityData entity = (EntityData) data
        entity.id == 42
        entity.entityName == "Animal"
        entity.actionName == CREATE
        entity.userMetadata == [ custom: true ]
        entity.associations == [ owner: [42], child: [1, 2]]
        entity.changes == [name: [null, "KnapKnap"], color: [null, "Brown"]]
    }
}