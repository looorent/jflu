package be.looorent.jflu.publisher.rabbitmq;

import be.looorent.jflu.Configuration;
import be.looorent.jflu.Event;
import be.looorent.jflu.RoutingKeyBuilder;
import be.looorent.jflu.publisher.EventPublisher;
import be.looorent.jflu.publisher.PublishingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

import static be.looorent.jflu.publisher.rabbitmq.RabbitMQPropertyName.EXCHANGE_DURABLE;
import static be.looorent.jflu.publisher.rabbitmq.RabbitMQPropertyName.EXCHANGE_NAME;
import static java.util.Optional.ofNullable;

/**
 * RabbitMQ implementation that connects an Exchange using a topic-based communication model
 * and that publishes events with routing keys (see {@link RoutingKeyBuilder}).
 * @author Lorent Lempereur {@literal <lorent.lempereur.dev@gmail.com>}
 */
public class RabbitMQEventTopicPublisher implements EventPublisher, AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(RabbitMQEventTopicPublisher.class);

    private static final String TOPIC_EXCHANGE_TYPE = "topic";
    private static final String DEFAULT_EXCHANGE_NAME = "jflu";
    private static final boolean DEFAULT_EXCHANGE_DURABLE = false;

    private Connection connection;
    private Channel channel;
    private String exchangeName;
    private boolean exchangeDurable;
    private ObjectMapper jsonMapper;

    /**
     * Build a publisher using properties, whatever their source.
     * @param properties contains properties from {@link RabbitMQPropertyName}
     */
    public RabbitMQEventTopicPublisher(Properties properties) {
        if (properties == null) {
            throw new IllegalArgumentException("properties must not be null");
        }

        try {
            jsonMapper = createJsonMapper();
            connection = new RabbitMQConnectionFactory().connect(properties);
            channel = connection.createChannel();
            exchangeName = ofNullable(EXCHANGE_NAME.readFrom(properties)).orElse(DEFAULT_EXCHANGE_NAME);
            exchangeDurable = ofNullable(EXCHANGE_DURABLE.readFrom(properties))
                    .filter(durable -> !durable.isEmpty())
                    .map(Boolean::parseBoolean)
                    .orElse(DEFAULT_EXCHANGE_DURABLE);
            LOG.info("Connect RabbitMQ with topic exchange type to exchange: {} with durability {}", exchangeName, exchangeDurable);
            channel.exchangeDeclare(exchangeName, TOPIC_EXCHANGE_TYPE, exchangeDurable);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void publish(Event event) throws PublishingException {
        String routingKey = createRoutingKeyFrom(event);
        LOG.debug("Publishing event {} with routing key : {}", event.getId(), routingKey);
        try {
            channel.basicPublish(exchangeName,
                    routingKey,
                    null,
                    jsonMapper.writeValueAsBytes(event));
        } catch (IOException e) {
            throw new PublishingException(e);
        }
    }

    @Override
    public void close() throws Exception {
        channel.close();
        connection.close();
    }

    protected ObjectMapper createJsonMapper() {
        return Configuration.getInstance().getDefaultJsonMapper();
    }

    protected String createRoutingKeyFrom(Event event) {
        return RoutingKeyBuilder.create()
                .withStatus(event.getStatus())
                .withEmitter(event.getEmitter())
                .withKind(event.getKind())
                .withName(event.getName())
                .build();
    }
}
