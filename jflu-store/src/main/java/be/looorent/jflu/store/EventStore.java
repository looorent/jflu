package be.looorent.jflu.store;

import be.looorent.jflu.subscriber.RabbitMQConfiguration;
import be.looorent.jflu.subscriber.RabbitMQListener;
import be.looorent.jflu.subscriber.RabbitMQSubscriptionRepository;
import be.looorent.jflu.subscriber.SubscriptionScanner;

import static be.looorent.jflu.subscriber.RabbitMQPropertyName.readPropertiesFromEnvironment;

/**
 * Startup class that registers a single projector to record each event into a store.
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
public class EventStore {

    public static void main(String... args) {

        listenToQueue();
    }

    private static void listenToQueue() {
        RabbitMQConfiguration configuration = new RabbitMQConfiguration(readPropertiesFromEnvironment());
        RabbitMQListener listener = new RabbitMQListener(configuration);
        RabbitMQSubscriptionRepository repository = new RabbitMQSubscriptionRepository(configuration);
        listener.start("be.looorent.jflu.store", new SubscriptionScanner(), repository);
    }
}
