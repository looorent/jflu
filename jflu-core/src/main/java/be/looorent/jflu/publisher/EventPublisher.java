package be.looorent.jflu.publisher;

import be.looorent.jflu.Event;

import java.util.Properties;

/**
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
public interface EventPublisher {

    void initialize(Properties properties);

    void publish(Event event) throws PublishingException;

}
