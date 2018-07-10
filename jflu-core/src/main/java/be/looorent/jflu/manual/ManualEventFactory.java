package be.looorent.jflu.manual;

import be.looorent.jflu.Configuration;
import be.looorent.jflu.Event;
import be.looorent.jflu.EventMetadata;

import java.util.Map;
import java.util.UUID;

import static be.looorent.jflu.EventKind.MANUAL;
import static be.looorent.jflu.EventStatus.NEW;
import static java.time.LocalDateTime.now;

/**
 * @author Lorent Lempereur {@literal <lorent.lempereur.dev@gmail.com>}
 */
public class ManualEventFactory {

    public Event createEvent(String name, Map<String, Object> data) {
        return new Event(createMetadata(name), new ManualData(data));
    }

    public Event createEvent(String name, ManualData data) {
        return new Event(createMetadata(name), data);
    }

    private EventMetadata createMetadata(String name) {
        return new EventMetadata(UUID.randomUUID(),
                name,
                Configuration.getInstance().getEmitter(),
                now(),
                MANUAL,
                NEW);
    }

}
