package be.looorent.jflu.subscriber;

/**
 * Start consuming a queue.
 * @author Lorent Lempereur {@literal <lorent.lempereur.dev@gmail.com>}
 */
public interface QueueListener {

    /**
     * Starts consuming a queue and apply {@link EventConsumer}s on each message read from the queue.
     * @param subscriptionRepository repository that can be used to find a set of {@link EventConsumer} to use for each event to consume; must not be null
     */
    void listen(SubscriptionRepository subscriptionRepository);

    /**
     * Stops consuming a queue. If the method "listen" has never occurred, nothing happens.
     * @throws IllegalArgumentException if the listener is not listening
     */
    void stop();

    /**
     * Deletes the queue
     */
    void deleteQueue();

    /**
     * @return true if the queue is listening; false otherwise
     */
    boolean isListening();
}
