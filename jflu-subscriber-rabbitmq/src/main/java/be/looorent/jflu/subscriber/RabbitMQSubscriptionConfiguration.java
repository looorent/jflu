package be.looorent.jflu.subscriber;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

import static be.looorent.jflu.subscriber.RabbitMQPropertyName.*;
import static java.util.Optional.ofNullable;

/**
 * This classes reads properties to give access to a dedicated RabbitMQ queue.
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
public class RabbitMQSubscriptionConfiguration implements BrokerSubscriptionConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(RabbitMQSubscriptionConfiguration.class);

    private static final String TOPIC_EXCHANGE_TYPE = "topic";
    private static final String DEFAULT_EXCHANGE_NAME = "jflu";
    private static final int DEFAULT_PREFETCH_SIZE = 10;

    private final Connection connection;
    private final Channel channel;
    private final String exchangeName;
    private final String queueName;

    public RabbitMQSubscriptionConfiguration(Properties properties) {
        try {
            connection = createFactory(properties).newConnection();
            channel = createChannel(connection, properties);
            queueName = createQueue(channel, properties);
            exchangeName = connectExchange(properties);
        } catch (IOException | TimeoutException e) {
            LOG.error("An error occurred when creating a connection to RabbitMQ", e);
            throw new RuntimeException(e);
        }
    }

    public static final RabbitMQSubscriptionConfiguration createFromSystemProperties() {
        return new RabbitMQSubscriptionConfiguration(readPropertiesFromEnvironment());
    }

    private String connectExchange(Properties properties) throws IOException {
        String exchangeName = ofNullable(EXCHANGE_NAME.readFrom(properties)).orElse(DEFAULT_EXCHANGE_NAME);
        channel.exchangeDeclare(exchangeName, TOPIC_EXCHANGE_TYPE);
        return exchangeName;
    }

    private String createQueue(Channel channel, Properties properties) throws IOException {
        AMQP.Queue.DeclareOk queueDeclaration = channel.queueDeclare(QUEUE_NAME.readFrom(properties), true, true, true, new HashMap<>());
        return queueDeclaration.getQueue();
    }

    private Channel createChannel(Connection connection, Properties properties) throws IOException {
        Channel channel = connection.createChannel();

        int prefetchSize = ofNullable(PREFETCH_SIZE.readFrom(properties))
                .map(Integer::parseInt)
                .orElse(DEFAULT_PREFETCH_SIZE);
        LOG.info("Prefetch size of queue is set to {}", prefetchSize);
        channel.basicQos(prefetchSize);
        return channel;
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

    public Channel getChannel() {
        return channel;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public String getQueueName() {
        return queueName;
    }

    @Override
    public SubscriptionRepository getSubscriptionRepository() {
        return new RabbitMQSubscriptionRepository(this);
    }

    @Override
    public QueueListener getQueueListener() {
        return new RabbitMQQueueListener(this);
    }
}
