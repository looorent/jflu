package be.looorent.jflu.request;

import be.looorent.jflu.Configuration;
import be.looorent.jflu.Event;
import be.looorent.jflu.EventMetadata;
import be.looorent.jflu.Payload;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static be.looorent.jflu.EventKind.REQUEST;
import static be.looorent.jflu.EventStatus.NEW;
import static java.time.LocalDateTime.now;
import static java.time.ZoneOffset.UTC;

/**
 * @author Lorent Lempereur {@literal <lorent.lempereur.dev@gmail.com>}
 */
public class RequestEventFactory {

    private final String emitter;

    public RequestEventFactory(String emitter) {
        this.emitter = emitter;
    }

    public RequestEventFactory() {
        this(Configuration.getInstance().getEmitter());
    }

    public Event createEvent(UUID requestId,
                             String controllerName,
                             String actionName,
                             String path,
                             int responseCode,
                             String userAgent,
                             int duration,
                             String overridenEmitter,
                             Map<String, List<String>> parameters,
                             Map<String, Payload> userMetadata) {
        return new Event(createMetadata(controllerName+"."+actionName),
                new RequestData(requestId,
                    controllerName,
                    actionName,
                    path,
                    responseCode,
                    userAgent,
                    duration,
                    overridenEmitter,
                    parameters,
                    userMetadata));
    }

    private EventMetadata createMetadata(String controllerName) {
        return new EventMetadata(UUID.randomUUID(),
                controllerName,
                emitter,
                now(UTC),
                REQUEST,
                NEW);
    }
}