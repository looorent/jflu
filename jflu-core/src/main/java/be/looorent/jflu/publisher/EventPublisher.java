package be.looorent.jflu.publisher;

import be.looorent.jflu.Event;

/**
 * Publishes an event to a message broker.
 * @author Lorent Lempereur {@literal <lorent.lempereur.dev@gmail.com>}
 */
public interface EventPublisher {

    /**
     * Publishes an event to a message broker.
     * @param event must not be null
     * @throws PublishingException if any publishing error occurs
     */
    void publish(Event event) throws PublishingException;

}
