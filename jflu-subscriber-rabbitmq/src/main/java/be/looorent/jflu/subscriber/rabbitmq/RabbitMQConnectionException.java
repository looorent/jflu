package be.looorent.jflu.subscriber.rabbitmq;

import be.looorent.jflu.subscriber.BrokerException;

/**
 * @author Lorent Lempereur {@literal <lorent.lempereur.dev@gmail.com>}
 */
public class RabbitMQConnectionException extends BrokerException {

    RabbitMQConnectionException(Exception cause) {
        super(cause);
    }
}
