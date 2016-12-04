package be.looorent.jflu.publisher;

import be.looorent.jflu.Configuration;
import be.looorent.jflu.Event;
import be.looorent.jflu.RoutingKeyBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

import static be.looorent.jflu.publisher.RabbitMQPropertyName.*;
import static java.util.Optional.ofNullable;

/**
 * RabbitMQ implementation that connects an Exchange using a topic-based communication model
 * and that publishes events with routing keys (see {@link RoutingKeyBuilder}).
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
public class RabbitMQEventTopicPublisher implements EventPublisher, AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(RabbitMQEventTopicPublisher.class);

    private static final String TOPIC_EXCHANGE_TYPE = "topic";
    private static final String DEFAULT_EXCHANGE_NAME = "jflu";

    private Connection connection;
    private Channel channel;
    private String exchangeName;
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
            connection = createFactory(properties).newConnection();
            channel = connection.createChannel();
            exchangeName = ofNullable(EXCHANGE_NAME.readFrom(properties)).orElse(DEFAULT_EXCHANGE_NAME);
            LOG.info("Connect RabbitMQ with topic exchange type to exchange: {}", exchangeName);
            channel.exchangeDeclare(exchangeName, TOPIC_EXCHANGE_TYPE);
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    private ConnectionFactory createFactory(Properties properties) {
        LOG.debug("Creating RabbitMQ connection factory with properties: {}", properties);
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername(USERNAME.readFrom(properties));
        factory.setPassword(PASSWORD.readFrom(properties));
        factory.setVirtualHost(VIRTUAL_HOST.readFrom(properties));
        factory.setHost(HOST.readFrom(properties));
        factory.setPort(Integer.parseInt(PORT.readFrom(properties)));
        return factory;
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
