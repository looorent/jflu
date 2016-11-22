package be.looorent.jflu.request;

import be.looorent.jflu.Configuration;
import be.looorent.jflu.Event;
import be.looorent.jflu.EventMetadata;

import java.util.Map;
import java.util.UUID;

import static be.looorent.jflu.EventKind.ENTITY_CHANGED;
import static be.looorent.jflu.EventStatus.NEW;
import static java.time.LocalDateTime.now;

/**
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
public class RequestEventFactory {

    public Event createEvent(String requestId,
                             String controllerName,
                             String actionName,
                             String path,
                             int responseCode,
                             String userAgent,
                             int duration,
                             Map<String, String[]> parameters) {
        return new Event(createMetadata(controllerName),
                new RequestData(requestId,
                    controllerName,
                    actionName,
                    path,
                    responseCode,
                    userAgent,
                    duration,
                    parameters));
    }

    private EventMetadata createMetadata(String controllerName) {
        return new EventMetadata(UUID.randomUUID(),
                controllerName,
                Configuration.getInstance().getEmitter(),
                now(),
                ENTITY_CHANGED,
                NEW);
    }
}