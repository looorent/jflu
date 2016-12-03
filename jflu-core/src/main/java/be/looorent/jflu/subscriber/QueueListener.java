package be.looorent.jflu.subscriber;

/**
 * Start consuming a queue.
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
public interface QueueListener {

    void listen(SubscriptionRepository subscriptionRepository);

}
