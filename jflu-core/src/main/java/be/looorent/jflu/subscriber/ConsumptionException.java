package be.looorent.jflu.subscriber;

import be.looorent.jflu.Configuration;
import be.looorent.jflu.Event;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Error that can occur during the consumption of an {@link Event}.
 * @author Lorent Lempereur {@literal <lorent.lempereur.dev@gmail.com>}
 */
public class ConsumptionException extends RuntimeException {

    private final Event event;
    private final String eventAsJson;

    ConsumptionException(Event event, Exception cause) {
        super(cause);
        if (event == null) {
            throw new IllegalArgumentException("event is mandatory when creating a ConsumptionException");
        }
        this.event = event;
        this.eventAsJson = serialize(event);
    }

    public Event getEvent() {
        return event;
    }

    public String getEventAsJson() {
        return eventAsJson;
    }

    private String serialize(Event event) {
        try {
            return Configuration.getInstance()
                    .getDefaultJsonMapper()
                    .writeValueAsString(event);
        } catch (JsonProcessingException e) {
            return "";
        }
    }
}
