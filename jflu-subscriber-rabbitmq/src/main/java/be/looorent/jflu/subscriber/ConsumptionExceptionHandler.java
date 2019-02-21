package be.looorent.jflu.subscriber;

/**
 * @author Logan Clément {@literal <logan@commuty.net>}
 */
public interface ConsumptionExceptionHandler {

    void handle(ConsumptionException exception);
}
