package be.looorent.jflu.subscriber.rabbitmq;

import be.looorent.jflu.subscriber.ConsumptionException;

/**
 * @author Logan Clement {@literal <logan@commuty.net>}
 */
public interface ConsumptionExceptionHandler {

    void handle(ConsumptionException exception);
}
