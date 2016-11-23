package be.looorent.jflu.publisher;

import be.looorent.jflu.Event;
import be.looorent.jflu.EventMetadata;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

import static java.util.Optional.ofNullable;

/**
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
public class RabbitMQEventPublisher implements EventPublisher, AutoCloseable {

    private static final String TOPIC_EXCHANGE_TYPE = "topic";
    private static final String DEFAULT_EXCHANGE_NAME = "jflu";
    public static final String ROUTING_KEY_SEPARATOR = ".";

    private Connection connection;
    private Channel channel;
    private String exchangeName;
    private ObjectMapper jsonMapper;

    @Override
    public void initialize(Properties properties) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername(properties.getProperty("rabbitmq.username"));
        factory.setPassword(properties.getProperty("rabbitmq.password"));
        factory.setVirtualHost(properties.getProperty("rabbitmq.virtualHost"));
        factory.setHost(properties.getProperty("rabbitmq.host"));
        factory.setPort(Integer.parseInt(properties.getProperty("rabbitmq.port")));
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            exchangeName = ofNullable(properties.getProperty("rabbitmq.exchangeName")).orElse(DEFAULT_EXCHANGE_NAME);
            channel.exchangeDeclare(exchangeName, TOPIC_EXCHANGE_TYPE);
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void publish(Event event) throws PublishingException {
        String routingKey = createRoutingKeyFrom(event);
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

    private String createRoutingKeyFrom(Event event) {
        StringBuilder builder = new StringBuilder();
        EventMetadata metadata = event.getMetadata();
        builder.append(metadata.getStatus().name().toLowerCase());
        builder.append(ROUTING_KEY_SEPARATOR);
        builder.append(metadata.getEventEmitter());
        builder.append(ROUTING_KEY_SEPARATOR);
        builder.append(metadata.getKind().name().toLowerCase());
        builder.append(ROUTING_KEY_SEPARATOR);
        builder.append(metadata.getName());
        return builder.toString();
    }
}
