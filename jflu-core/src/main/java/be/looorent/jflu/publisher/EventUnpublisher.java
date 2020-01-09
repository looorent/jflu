package be.looorent.jflu.publisher;

import be.looorent.jflu.Event;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Default implementation that does not publish any event anywhere.
 * @author Lorent Lempereur {@literal <lorent.lempereur.dev@gmail.com>}
 */
public class EventUnpublisher implements EventPublisher {
    private static final Logger LOG = getLogger(EventUnpublisher.class);
    @Override
    public void publish(Event event) {
        LOG.trace("Event is not published: {}", event);
    }
}
