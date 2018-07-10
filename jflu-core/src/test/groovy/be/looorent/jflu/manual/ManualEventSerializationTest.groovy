package be.looorent.jflu.manual

import be.looorent.jflu.Configuration
import be.looorent.jflu.Event
import be.looorent.jflu.EventData
import be.looorent.jflu.EventMetadata
import com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Specification

import static be.looorent.jflu.EventKind.MANUAL
import static be.looorent.jflu.EventStatus.NEW
import static java.time.LocalDateTime.of
import static java.time.Month.JULY
import static java.util.UUID.randomUUID

/**
 * @author Lorent Lempereur {@literal <lorent.lempereur.dev@gmail.com>}
 */
class ManualEventSerializationTest extends Specification {

    def "parsing an event from a JSON file works"() {
        when: "an event is parsed from a JSON file"
        ObjectMapper jsonMapper = Configuration.instance.defaultJsonMapper
        Event event = this.class.getResource("manual_event.json").withInputStream { inputStream ->
            jsonMapper.readValue inputStream, Event
        }

        then: "this event has correct properties"
        EventMetadata metadata = event.metadata
        EventData data = event.data

        metadata.id.toString() == "25c0e636-bd3e-45a0-8d7b-3371678bb810"
        metadata.name == "clean manually"
        metadata.emitter == "another-micro-service"
        metadata.timestamp == of(2018, JULY, 5, 18, 25, 43)
        metadata.kind == MANUAL
        metadata.status == NEW

        data instanceof ManualData
        def manualData = (ManualData) data
        manualData.get("a") == 1
        manualData.get("b") == "requestId"
        manualData.get("c") instanceof Map
        def cData = (Map<String, Object>) manualData.get("c")
        cData.get("e") == [1, 2, 3]
        cData.get("f") instanceof Map
        def fData = (Map<String, Object>) cData.get("f")
        fData.get("g") == false
        fData.get("h") == "y"
        fData.get("i") == 12

    }

    def "parsing an event from a JSON (without data.type specified) file works"() {
        when: "an event is parsed from a JSON file"
        ObjectMapper jsonMapper = Configuration.instance.defaultJsonMapper
        Event event = this.class.getResource("manual_event_without_specific_type.json").withInputStream { inputStream ->
            jsonMapper.readValue inputStream, Event
        }

        then: "this event has correct properties"
        EventData data = event.data

        data instanceof ManualData
        def manualData = (ManualData) data
        manualData.get("a") == 1
        manualData.get("b") == "requestId"
    }

    def "json mapper is bi-directional"() {
        given: "a typical event"
        Event event = new ManualEventFactory().createEvent("clean data manually",
            [
                    a: 1,
                    b: 2,
                    c: false,
                    d: [1, 10, 100]
            ])

        when: "marshalling and unmarshalling this event"
        ObjectMapper jsonMapper = Configuration.instance.defaultJsonMapper
        Event parsedEvent = jsonMapper.readValue jsonMapper.writeValueAsBytes(event), Event

        then: "this event is identical to the original one"
        EventMetadata parsedMetadata = parsedEvent.metadata
        parsedEvent.data instanceof ManualData
        EventMetadata metadata = event.metadata
        event.data instanceof ManualData
        ManualData data = (ManualData) parsedEvent.data

        parsedMetadata.id           == metadata.id
        parsedMetadata.emitter      == metadata.emitter
        parsedMetadata.kind         == metadata.kind
        parsedMetadata.name         == metadata.name
        parsedMetadata.status       == metadata.status
        parsedMetadata.timestamp    == metadata.timestamp
        data                        == event.data
    }

}