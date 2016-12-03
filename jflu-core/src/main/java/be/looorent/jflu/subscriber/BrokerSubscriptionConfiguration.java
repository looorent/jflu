package be.looorent.jflu.subscriber;

/**
 * Defines all properties to connect a message broker.
 * This configuration focuses on the consumption of messages.
 *
 * This implementation must provide two services:
 * <ul>
 *     <li>An implementation of {@link SubscriptionRepository} that provides some {@link EventConsumer} for each event.</li>
 *     <li>An implementation of {@link QueueListener} to define how to connect a queue and consume its messages.</li>
 * </ul>
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
public interface BrokerSubscriptionConfiguration {

    /**
     * @return an implementation of {@link SubscriptionRepository} that provides some {@link EventConsumer} for each event.
     */
    SubscriptionRepository getSubscriptionRepository();

    /**
     * @return an implementation of {@link QueueListener} to define how to connect a queue and consume its messages.
     */
    QueueListener getQueueListener();
}
