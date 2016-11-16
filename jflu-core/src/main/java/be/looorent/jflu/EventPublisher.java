package be.looorent.jflu;

import java.util.Properties;

/**
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
public interface EventPublisher {

    void initialize(Properties properties);

    void publish(Event event);

}
