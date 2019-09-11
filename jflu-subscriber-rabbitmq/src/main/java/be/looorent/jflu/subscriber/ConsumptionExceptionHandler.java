package be.looorent.jflu.subscriber;

/**
 * @author Logan Clement {@literal <logan@commuty.net>}
 */
public interface ConsumptionExceptionHandler {

    void handle(ConsumptionException exception);
}
