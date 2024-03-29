package be.looorent.jflu.entity

import be.looorent.jflu.*
import com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalDateTime

import static be.looorent.jflu.EventKind.ENTITY_CHANGE
import static be.looorent.jflu.EventStatus.NEW
import static be.looorent.jflu.entity.EntityActionName.CREATE
import static java.time.Month.APRIL
import static java.util.Collections.emptyMap
import static java.util.Optional.empty
import static java.util.Optional.of
import static java.util.UUID.randomUUID

/**
 * @author Lorent Lempereur {@literal <lorent.lempereur.dev@gmail.com>}
 */
class EntityEventSerializationTest extends Specification {
    ObjectMapper jsonMapper = Configuration.instance.defaultJsonMapper

    def "json mapper is bi-directional"() {
        given: "a typical updated entity event"
        Event event = new EntityEventFactory().createEventOnUpdate(
                String.class,
                42,
                [
                        "type": ["Bus", "Autocar"],
                        "maxSpeed" : [null, 90l],
                        "editedAt": ["2018-07-01T00:00:00Z", null]
                ],
                randomUUID()
        )

        when: "marshalling and unmashalling this event"
        Event parsedEvent = jsonMapper.readValue jsonMapper.writeValueAsBytes(event), Event

        then: "this event is identical to the original one"
        EventMetadata parsedMetadata = parsedEvent.metadata
        parsedEvent.data instanceof EntityData
        EntityData parsedEntity = parsedEvent.data as EntityData
        EventMetadata metadata = event.metadata
        event.data instanceof EntityData
        EntityData entity = event.data as EntityData

        parsedMetadata.id           == metadata.id
        parsedMetadata.emitter      == metadata.emitter
        parsedMetadata.kind         == metadata.kind
        parsedMetadata.name         == metadata.name
        parsedMetadata.status       == metadata.status
        parsedMetadata.timestamp    == metadata.timestamp


        parsedEntity.id                      == entity.id
        parsedEntity.requestId               == entity.requestId
        parsedEntity.requestMetadata         == emptyMap()
        parsedEntity.entityName              == entity.entityName
        parsedEntity.actionName              == entity.actionName
        parsedEntity.changes.size()          == entity.changes.size()
        parsedEntity.changes["type"].changes == entity.changes["type"].changes
        parsedEntity.changes["maxSpeed"].beforeValue(Long.class) == entity.changes["maxSpeed"].beforeValue(Long.class)
        parsedEntity.changes["maxSpeed"].afterValue(Long.class)  == entity.changes["maxSpeed"].afterValue(Long.class)
        parsedEntity.changes["editedAt"].beforeValue(LocalDateTime.class)  == entity.changes["editedAt"].beforeValue(LocalDateTime.class)
    }



    def "parsing an event from a JSON file works"() {
        when: "an event is parsed from a JSON file"
        Event event = this.class.getResource("entity_event.json").withInputStream { inputStream ->
            jsonMapper.readValue inputStream, Event
        }

        then: "this event has correct properties"
        EventMetadata metadata = event.metadata
        EventData data = event.data

        metadata.id.toString() == "0d5ce258-1314-45d9-8ace-ce43d42255cc"
        metadata.name == "create animal"
        metadata.emitter == "pet-store"
        metadata.timestamp == LocalDateTime.of(2016, APRIL, 23, 18, 25, 43)
        metadata.kind == ENTITY_CHANGE
        metadata.status == NEW

        data instanceof EntityData
        EntityData entity = (EntityData) data
        entity.id == 42
        entity.requestId == null
        entity.requestMetadata == emptyMap()
        entity.entityName == "Animal"
        entity.actionName == CREATE
        entity.userMetadata == [ custom: new Payload(true) ]
        entity.associations == [ owner_id: 42, owner_type: "User", child_id: 1, unknown: 1 ]
        entity.associationIds == [ owner_id: 42, child_id: 1 ]
        entity.associationTypes == [ owner_type: "User" ]
        entity.changes == [name: new EntityChange([null, new Payload("KnapKnap")]), color: new EntityChange([null, new Payload("Brown")])]
    }


    def "parsing an event from a JSON file (including request metadata) works"() {
        when: "an event is parsed from a JSON file"
        Event event = this.class.getResource("entity_event_with_request_metadata.json").withInputStream { inputStream ->
            jsonMapper.readValue inputStream, Event
        }

        then: "this event has correct properties"
        EventMetadata metadata = event.metadata
        EventData data = event.data

        metadata.id.toString() == "f9a218b1-e772-4a9f-ac62-3247a02c41a1"
        data instanceof EntityData
        EntityData entity = (EntityData) data
        entity.id == 42
        entity.requestId == UUID.fromString("68d39f52-2760-40a8-bbba-a85d7a5fbb01")
        entity.requestMetadata.size() == 3
        entity.requestMetadata.get("delegate").getPayload() == true
        entity.requestMetadata.get("path").getPayload() == "/animals"
        entity.requestMetadata.get("currentUserId").getPayload() == 1234
    }

    def "parking an event with a String entityId works"() {
        when: "an event is parsed from a JSON file"
        Event event = this.class.getResource("entity_event_ruby_utc.json").withInputStream { inputStream ->
            jsonMapper.readValue inputStream, Event
        }

        then: "The event has correct properties"
        def data = event.data as EntityData
        data.id == "ID-1234"
    }

    def "parsing an event generated by Ruby works"() {
        when: "an event is parsed from a JSON file"
        Event event = this.class.getResource("entity_event_ruby_utc.json").withInputStream { inputStream ->
            jsonMapper.readValue inputStream, Event
        }

        then: "this event has correct properties and the date is successfully parsed"
        EventMetadata metadata = event.metadata

        metadata.id.toString() == "0d5ce258-1314-45d9-8ace-ce43d42255cc"
        metadata.name == "create animal"
        metadata.emitter == "pet-store"
        metadata.timestamp == LocalDateTime.of(2016, APRIL, 23, 18, 25, 43)
        metadata.kind == ENTITY_CHANGE
        metadata.status == NEW
    }

    def "parsing an event generated by Ruby with a zoned date works"() {
        when: "an event is parsed from a JSON file"
        Event event = this.class.getResource("entity_event_ruby_zoned.json").withInputStream { inputStream ->
            jsonMapper.readValue inputStream, Event
        }

        then: "this event has correct properties and the date is successfully parsed"
        EventMetadata metadata = event.metadata

        metadata.id.toString() == "0d5ce258-1314-45d9-8ace-ce43d42255cc"
        metadata.name == "create animal"
        metadata.emitter == "pet-store"
        metadata.timestamp == LocalDateTime.of(2016, APRIL, 23, 18, 25, 43)
        metadata.kind == ENTITY_CHANGE
        metadata.status == NEW
    }

    def "parsing multiple values for userMetadata returns the right type"() {
        when: "an event is parsed from a JSON file"
        Event event = this.class.getResource("entity_event_multiple_metadata.json").withInputStream { inputStream ->
            jsonMapper.readValue inputStream, Event
        }

        then: "this event is parseable and metadata is queryable"
        EntityData entityData = event.data as EntityData
        entityData.userMetadata["aString"].get(String) == of("pouet")
        entityData.userMetadata["aLong"].get(Long) == of(42L)
        entityData.userMetadata["aUUID"].get(UUID) == of(UUID.fromString("5546fe3d-b2b2-48c4-a794-2ac02dba888d"))
        entityData.userMetadata["a8601Date"].get(LocalDateTime) == of(LocalDateTime.of(2018,7,19,8,14,3))
        entityData.userMetadata["aRubyDate"].get(LocalDateTime) == of(LocalDateTime.of(2018,7,19,13,27,30))
        entityData.userMetadata["aRubyDay"].get(LocalDate) == of(LocalDate.of(2021,9,20))
    }

    def "parsing multiple changes return the right type"() {
        when: "an event is parsed from a JSON file"
        Event event = this.class.getResource("entity_event_multiple_changes.json").withInputStream { inputStream ->
            jsonMapper.readValue inputStream, Event
        }

        then: "this event is parseable and changes is queryable"
        def entityData = event.data as EntityData
        def changes = entityData.changes
        changes.size() == 4
        changes["name"].beforeValue(String) == empty()
        changes["name"].afterValue(String) == of("KnapKnap")
        changes["kind"].beforeValue(String) == of("Dog")
        changes["kind"].afterValue(String) == of("Cat")
        changes["age"].beforeValue(Long) == of(7L)
        changes["age"].afterValue(Long) == of(8L)
        changes["someDate"].beforeValue(LocalDateTime) == of(LocalDateTime.of(2018,7,20,0,0,0))
        changes["someDate"].afterValue(LocalDateTime) == empty()
    }
}