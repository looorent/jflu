package be.looorent.jflu.subscriber;

/**
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
public class RabbitMQConnectionException extends BrokerException {

    RabbitMQConnectionException(Exception cause) {
        super(cause);
    }
}
