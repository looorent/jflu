package be.looorent.jflu.subscriber.rabbitmq;

import be.looorent.jflu.Configuration;
import be.looorent.jflu.Event;
import be.looorent.jflu.subscriber.ConsumptionException;
import be.looorent.jflu.subscriber.QueueListener;
import be.looorent.jflu.subscriber.SubscriptionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * RabbitMQ implementation to consume messages from a dedicated queue.
 * @author Lorent Lempereur {@literal <lorent.lempereur.dev@gmail.com>}
 */
class RabbitMQQueueListener implements QueueListener {

    private static final Logger LOG = LoggerFactory.getLogger(RabbitMQQueueListener.class);

    private final RabbitMQSubscriptionConfiguration configuration;
    private final ObjectMapper jsonMapper;

    public RabbitMQQueueListener(RabbitMQSubscriptionConfiguration configuration) {
        if (configuration == null) {
            throw new IllegalArgumentException("configuration must not be null");
        }
        this.configuration = configuration;
        this.jsonMapper = Configuration.getInstance().getDefaultJsonMapper();
    }

    @Override
    public void listen(final SubscriptionRepository subscriptionRepository) {
        if (subscriptionRepository == null) {
            throw new IllegalArgumentException("subscriptionRepository must not be null");
        }

        try {
            final Channel channel = configuration.getChannel();

            channel.basicConsume(configuration.getQueueName(), false, new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag,
                                           Envelope envelope,
                                           AMQP.BasicProperties properties,
                                           byte[] body) throws IOException {
                    super.handleDelivery(consumerTag, envelope, properties, body);
                    Event event = jsonMapper.readValue(body, Event.class);
                    try {
                        subscriptionRepository.findAllSubscriptionsFor(event)
                                .forEach(subscription -> subscription.consume(event));

                        channel.basicAck(envelope.getDeliveryTag(), false);
                        LOG.debug("Event acked: {}", event.getId());
                    } catch (Exception e) {
                        throw new ConsumptionException(event, e);
                    }
                }
            });
        } catch (IOException e) {
            LOG.error("An error occurred when consuming events", e);
            throw new RuntimeException(e);
        }
    }
}
