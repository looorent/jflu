package be.looorent.jflu.subscriber;

import be.looorent.jflu.Configuration;
import be.looorent.jflu.Event;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
public class RabbitMQListener extends EventListener {

    private static final Logger LOG = LoggerFactory.getLogger(RabbitMQListener.class);

    private final RabbitMQConfiguration configuration;
    private final ObjectMapper jsonMapper;

    public RabbitMQListener(RabbitMQConfiguration configuration) {
        if (configuration == null) {
            throw new IllegalArgumentException("configuration must not be null");
        }
        this.configuration = configuration;
        this.jsonMapper = Configuration.getInstance().getDefaultJsonMapper();
    }

    @Override
    protected void startConsumers(final SubscriptionRepository subscriptionRepository) {
        try {
            configuration.getChannel().basicConsume(configuration.getQueueName(), true, new DefaultConsumer(configuration.getChannel()) {
                @Override
                public void handleDelivery(String consumerTag,
                                           Envelope envelope,
                                           AMQP.BasicProperties properties,
                                           byte[] body) throws IOException {
                    super.handleDelivery(consumerTag, envelope, properties, body);
                    Event event = jsonMapper.readValue(body, Event.class);
                    for (Subscription subscription : subscriptionRepository.findAllSubscriptionsFor(event)) {
                        LOG.debug("Consuming event {} with consumer {}", event.getId(), subscription.getName());
                        subscription.getProjector().accept(event);
                    }
                }
            });
        } catch (IOException e) {
            LOG.error("An error occurred when consuming events", e);
            throw new RuntimeException(e);
        }
    }
}
