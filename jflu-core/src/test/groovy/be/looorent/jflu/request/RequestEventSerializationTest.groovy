package be.looorent.jflu.request

import be.looorent.jflu.Configuration
import be.looorent.jflu.Event
import be.looorent.jflu.EventData
import be.looorent.jflu.EventKind
import be.looorent.jflu.EventMetadata
import be.looorent.jflu.EventStatus
import be.looorent.jflu.entity.EntityData
import com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Specification

import java.time.LocalDateTime
import java.time.Month

import static be.looorent.jflu.EventKind.ENTITY_CHANGED
import static be.looorent.jflu.EventStatus.NEW
import static java.time.LocalDateTime.of
import static java.time.Month.APRIL

/**
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
class RequestEventSerializationTest extends Specification {

    def "parsing an event from a JSON file works"() {
        when: "an event is parsed from a JSON file"
        ObjectMapper jsonMapper = Configuration.instance.defaultJsonMapper
        Event event = this.class.getResource("request_event.json").withInputStream { inputStream ->
            jsonMapper.readValue inputStream, Event
        }

        then: "this event has correct properties"
        EventMetadata metadata = event.metadata
        EventData data = event.data

        metadata.id.toString() == "0d5ce258-1314-45d9-8ace-ce43d42255cc"
        metadata.name == "create pouetpouet"
        metadata.eventEmitter == "jflu"
        metadata.timestamp == of(2016, APRIL, 23, 18, 25, 43)
        metadata.kind == ENTITY_CHANGED
        metadata.status == NEW

        data instanceof RequestData
        RequestData request = (RequestData) data
        request.requestId == "requestId"
        request.controllerName == "pouetpouet"
        request.actionName == "create"
        request.path == "http://localhost:8080/pouetpouet"
        request.responseCode == 200
        request.userAgent == "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.98 Safari/537.36"
        request.duration == 12
        request.parameters.size() == 1
        request.parameters.get("type")[0] == "coincoin"

    }

}