package be.looorent.jflu.subscriber;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

import static be.looorent.jflu.subscriber.RabbitMQPropertyName.*;
import static java.util.Optional.ofNullable;

/**
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
public class RabbitMQConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(RabbitMQConfiguration.class);

    private static final String TOPIC_EXCHANGE_TYPE = "topic";
    private static final String DEFAULT_EXCHANGE_NAME = "jflu";

    private final Connection connection;
    private final Channel channel;
    private final String exchangeName;
    private final String queueName;

    public RabbitMQConfiguration(Properties properties) {
        try {
            connection = createFactory(properties).newConnection();
            channel = connection.createChannel();
            AMQP.Queue.DeclareOk queueDeclaration = channel.queueDeclare(QUEUE_NAME.readFrom(properties), true, true, true, new HashMap<>());
            this.queueName = queueDeclaration.getQueue();
            exchangeName = ofNullable(EXCHANGE_NAME.readFrom(properties)).orElse(DEFAULT_EXCHANGE_NAME);
            channel.exchangeDeclare(exchangeName, TOPIC_EXCHANGE_TYPE);
        } catch (IOException | TimeoutException e) {
            LOG.error("An error occurred when creating a connection to RabbitMQ", e);
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

    public Connection getConnection() {
        return connection;
    }

    public Channel getChannel() {
        return channel;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public String getQueueName() {
        return queueName;
    }
}
