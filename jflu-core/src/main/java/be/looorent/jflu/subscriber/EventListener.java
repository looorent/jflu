package be.looorent.jflu.subscriber;

/**
 * Bootstrap class to connect a message broker and start a set of {@link EventConsumer}s to consume its messages.
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
public class EventListener {

    /**
     * <ul>
     *     <li>Scans classpath (based on <code>projectorsPackage</code> to register all {@link EventConsumer}s into the repository.</li>
     *     <li>Start consuming events from a broker</li>
     * </ul>
     *
     * Consuming events depends on the implementation provided
     * @param projectorsPackage must not be null or empty
     * @param subscriptionScanner must not be null
     * @param subscriptionRepository must not be null
     * @param queueListener must not be null
     */
    public void start(String projectorsPackage,
                      SubscriptionScanner subscriptionScanner,
                      SubscriptionRepository subscriptionRepository,
                      QueueListener queueListener) {
        if (projectorsPackage == null || projectorsPackage.isEmpty()) {
            throw new IllegalArgumentException("projectorPackage must not be null or empty");
        }

        if (subscriptionScanner == null) {
            throw new IllegalArgumentException("subscriptionScanner must not be null or empty");
        }

        if (subscriptionRepository == null) {
            throw new IllegalArgumentException("subscriptionRepository must not be null or empty");
        }

        if (queueListener == null) {
            throw new IllegalArgumentException("queueListener must not be null or empty");
        }

        subscriptionScanner.findAllSubscriptionsIn(projectorsPackage)
                .forEach(subscriptionRepository::register);
        queueListener.listen(subscriptionRepository);
    }
}
