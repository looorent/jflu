package be.looorent.jflu.subscriber;

/**
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
public interface BrokerSubscriptionConfiguration {

    SubscriptionRepository getSubscriptionRepository();

    QueueListener getQueueListener();
}
