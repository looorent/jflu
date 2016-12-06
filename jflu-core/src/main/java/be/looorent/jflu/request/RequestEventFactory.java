package be.looorent.jflu.request;

import be.looorent.jflu.Configuration;
import be.looorent.jflu.Event;
import be.looorent.jflu.EventMetadata;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static be.looorent.jflu.EventKind.ENTITY_CHANGE;
import static be.looorent.jflu.EventKind.REQUEST;
import static be.looorent.jflu.EventStatus.NEW;
import static java.time.LocalDateTime.now;

/**
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
public class RequestEventFactory {

    public Event createEvent(UUID requestId,
                             String controllerName,
                             String actionName,
                             String path,
                             int responseCode,
                             String userAgent,
                             int duration,
                             Map<String, List<String>> parameters) {
        return new Event(createMetadata(controllerName+"."+actionName),
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
                REQUEST,
                NEW);
    }
}