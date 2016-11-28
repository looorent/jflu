package be.looorent.jflu.subscriber;

/**
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
public abstract class EventListener {

    public void start(String projectorsPackage,
                      SubscriptionScanner subscriptionScanner,
                      SubscriptionRepository subscriptionRepository) {
        subscriptionScanner.findAllSubscriptions(projectorsPackage)
                .forEach(subscriptionRepository::register);
    }

    protected abstract void startConsumers(SubscriptionRepository subscriptionRepository);
}
