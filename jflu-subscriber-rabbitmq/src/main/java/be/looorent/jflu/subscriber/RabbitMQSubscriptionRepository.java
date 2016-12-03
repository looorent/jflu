package be.looorent.jflu.subscriber;

import be.looorent.jflu.RoutingKeyBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Decorator that adds some RabbitMQ details on the {@link SubscriptionRepository}.
 * Each time a subscription is registered, the RabbitMQ queue is bound to a routing key
 * defined by a {@link SubscriptionQuery}.
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
class RabbitMQSubscriptionRepository extends SubscriptionRepository {

    private static final Logger LOG = LoggerFactory.getLogger(RabbitMQSubscriptionRepository.class);

    private final RabbitMQSubscriptionConfiguration configuration;

    public RabbitMQSubscriptionRepository(RabbitMQSubscriptionConfiguration configuration) {
        super();
        if (configuration == null) {
            throw new IllegalArgumentException("configuration must not be null");
        }
        this.configuration = configuration;
    }

    @Override
    public void register(Subscription subscription) {
        if (subscription == null) {
            throw new IllegalArgumentException("subscription must not be null");
        }

        String queue = configuration.getQueueName();
        String exchange = configuration.getExchangeName();
        try {
            String routingKey = convertToRoutingKey(subscription.getQuery());
            LOG.info("Binding RabbitMQ Queue '{}' to Exchange '{}' using routing key: {}", queue, exchange, routingKey);
            configuration.getChannel().queueBind(queue,
                    exchange,
                    routingKey);
        } catch (IOException e) {
            LOG.error("An error occurred when binding RabbitMQ Queue '{}' to Exchange '{}' for subscription: {}", queue, exchange, subscription.getName(), e);
            throw new IllegalArgumentException(e);
        }
        super.register(subscription);
    }

    protected String convertToRoutingKey(SubscriptionQuery query) {
        return RoutingKeyBuilder.create()
                .withStatus(query.getStatus())
                .withEmitter(query.getEmitter())
                .withKind(query.getKind())
                .withName(query.getName())
                .build();
    }
}
