package be.looorent.jflu.request

import be.looorent.jflu.Configuration
import be.looorent.jflu.Event
import be.looorent.jflu.EventData
import be.looorent.jflu.EventMetadata
import be.looorent.jflu.Payload
import com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Specification

import static be.looorent.jflu.EventKind.REQUEST
import static be.looorent.jflu.EventStatus.NEW
import static java.time.LocalDateTime.of
import static java.time.Month.APRIL
import static java.util.UUID.randomUUID

/**
 * @author Lorent Lempereur {@literal <lorent.lempereur.dev@gmail.com>}
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
        metadata.emitter == "jflu"
        metadata.timestamp == of(2016, APRIL, 23, 18, 25, 43)
        metadata.kind == REQUEST
        metadata.status == NEW

        data instanceof RequestData
        RequestData request = (RequestData) data
        request.requestId == UUID.fromString("2f0a07e2-70e5-44e6-9875-6fc46a3ab887")
        request.controllerName == "pouetpouet"
        request.actionName == "create"
        request.path == "http://localhost:8080/pouetpouet"
        request.responseCode == 200
        request.userAgent == "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.98 Safari/537.36"
        request.duration == 12
        request.parameters.size() == 1
        request.parameters.get("type")[0] == "coincoin"

        request.userMetadata.size() == 2
        request.userMetadata.get("pou").get(String) == "eeeeet"
        request.userMetadata.get("hii").get(String) == "han"

    }

    def "json mapper is bi-directional"() {
        given: "a typical event"
        Event event = new RequestEventFactory().createEvent(randomUUID(),
            "controllerName",
            "actionName",
            "path",
            500,
            "userAgent",
            200,
            [
                    "name": ["pouet"],
                    "values": ["1", "2", "3"]
            ],
            [
                    "action": new Payload("plouf")
            ])

        when: "marshalling and unmarshalling this event"
        ObjectMapper jsonMapper = Configuration.instance.defaultJsonMapper
        Event parsedEvent = jsonMapper.readValue jsonMapper.writeValueAsBytes(event), Event

        then: "this event is identical to the original one"
        EventMetadata parsedMetadata = parsedEvent.metadata
        parsedEvent.data instanceof RequestData
        RequestData parsedRequest = (RequestData) parsedEvent.data
        EventMetadata metadata = event.metadata
        event.data instanceof RequestData
        RequestData request = (RequestData) event.data

        parsedMetadata.id           == metadata.id
        parsedMetadata.emitter      == metadata.emitter
        parsedMetadata.kind         == metadata.kind
        parsedMetadata.name         == metadata.name
        parsedMetadata.status       == metadata.status
        parsedMetadata.timestamp    == metadata.timestamp

        parsedRequest.requestId                          == request.requestId
        parsedRequest.controllerName                     == request.controllerName
        parsedRequest.actionName                         == request.actionName
        parsedRequest.path                               == request.path
        parsedRequest.responseCode                       == request.responseCode
        parsedRequest.userAgent                          == request.userAgent
        parsedRequest.duration                           == request.duration
        parsedRequest.parameters.size()                  == request.parameters.size()
        parsedRequest.userMetadata["action"].payload     == request.userMetadata["action"].payload
    }

}