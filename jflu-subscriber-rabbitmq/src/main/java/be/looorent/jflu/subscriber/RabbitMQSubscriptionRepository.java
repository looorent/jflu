package be.looorent.jflu.subscriber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
public class RabbitMQSubscriptionRepository extends SubscriptionRepository {

    private static final Logger LOG = LoggerFactory.getLogger(RabbitMQSubscriptionRepository.class);
    private static final String ROUTING_KEY_SEPARATOR = ".";

    private final RabbitMQConfiguration configuration;

    public RabbitMQSubscriptionRepository(RabbitMQConfiguration configuration) {
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
        try {
            configuration.getChannel().queueBind(configuration.getQueueName(),
                                                 configuration.getExchangeName(),
                                                 convertToRoutingKey(subscription.getQuery()));
        } catch (IOException e) {
            LOG.error("An error occurred when binding a subscription ({}) to queue: {}",
                    subscription.getName(),
                    configuration.getQueueName(),
                    e);
            throw new IllegalArgumentException(e);
        }
        super.register(subscription);
    }

    protected String convertToRoutingKey(SubscriptionQuery query) {
        StringBuilder builder = new StringBuilder();
        builder.append(query.matchesAllStatuses() ? "*" : query.getStatus().name().toLowerCase());
        builder.append(ROUTING_KEY_SEPARATOR);
        builder.append(query.matchesAllEmitters() ? "*" : query.getEmitter());
        builder.append(ROUTING_KEY_SEPARATOR);
        builder.append(query.matchesAllKinds() ? "*" : query.getKind().name().toLowerCase());
        builder.append(ROUTING_KEY_SEPARATOR);
        builder.append(query.matchesAllNames() ? "*" : query.getName());
        return builder.toString();
    }
}
