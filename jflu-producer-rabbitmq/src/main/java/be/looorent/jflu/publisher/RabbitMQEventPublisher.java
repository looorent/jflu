package be.looorent.jflu.publisher;

import be.looorent.jflu.Configuration;
import be.looorent.jflu.Event;
import be.looorent.jflu.EventMetadata;
import be.looorent.jflu.RoutingKeyBuilder;
import be.looorent.jflu.subscriber.SubscriptionQuery;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static be.looorent.jflu.publisher.RabbitMQPropertyName.*;
import static java.util.Optional.ofNullable;

/**
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
public class RabbitMQEventPublisher implements EventPublisher, AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(RabbitMQEventPublisher.class);

    private static final String TOPIC_EXCHANGE_TYPE = "topic";
    private static final String DEFAULT_EXCHANGE_NAME = "jflu";
    private static final String ROUTING_KEY_SEPARATOR = ".";

    private Connection connection;
    private Channel channel;
    private String exchangeName;
    private ObjectMapper jsonMapper;

    @Override
    public void initialize(Properties properties) {
        try {
            jsonMapper = createJsonMapper();
            connection = createFactory(properties).newConnection();
            channel = connection.createChannel();
            exchangeName = ofNullable(EXCHANGE_NAME.readFrom(properties)).orElse(DEFAULT_EXCHANGE_NAME);
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
        EventMetadata metadata = event.getMetadata();
        return RoutingKeyBuilder.create()
                .withStatus(event.getStatus())
                .withEmitter(event.getEventEmitter())
                .withKind(event.getKind())
                .withName(event.getName())
                .build();
    }
}
