package be.looorent.jflu.subscriber.rabbitmq;

import be.looorent.jflu.subscriber.BrokerSubscriptionConfiguration;
import be.looorent.jflu.subscriber.QueueListener;
import be.looorent.jflu.subscriber.SubscriptionRepository;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import static be.looorent.jflu.subscriber.rabbitmq.RabbitMQPropertyName.*;
import static java.lang.Integer.parseInt;
import static java.util.Optional.ofNullable;

/**
 * This classes reads properties to give access to a dedicated RabbitMQ queue.
 * @author Lorent Lempereur {@literal <lorent.lempereur.dev@gmail.com>}
 */
public class RabbitMQSubscriptionConfiguration implements BrokerSubscriptionConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(RabbitMQSubscriptionConfiguration.class);

    private static final String TOPIC_EXCHANGE_TYPE = "topic";
    private static final String DEFAULT_EXCHANGE_NAME = "jflu";
    private static final int DEFAULT_PREFETCH_SIZE = 10;
    private static final boolean DEFAULT_QUEUE_DURABILITY = false;

    private final Connection connection;
    private final Channel channel;
    private final String exchangeName;
    private final String queueName;

    public RabbitMQSubscriptionConfiguration(Properties properties, ConsumptionExceptionHandler exceptionHandler) throws RabbitMQConnectionException {
        try {
            connection = new RabbitMQConnectionFactory(exceptionHandler).connect(properties);
            channel = createChannel(connection, properties);
            queueName = createQueue(channel, properties);
            exchangeName = connectExchange(properties);
        } catch (Exception e) {
            LOG.error("An error occurred when creating a connection to RabbitMQ", e);
            throw new RabbitMQConnectionException(e);
        }
    }

    public static final RabbitMQSubscriptionConfiguration createFromSystemProperties() throws RabbitMQConnectionException {
        return createFromSystemProperties(null);
    }

    public static final RabbitMQSubscriptionConfiguration createFromSystemProperties(ConsumptionExceptionHandler exceptionHandler) throws RabbitMQConnectionException {
        return new RabbitMQSubscriptionConfiguration(readPropertiesFromEnvironment(), exceptionHandler);
    }

    private String connectExchange(Properties properties) throws IOException {
        String exchangeName = ofNullable(EXCHANGE_NAME.readFrom(properties)).orElse(DEFAULT_EXCHANGE_NAME);
        boolean durableQueue = ofNullable(DURABLE_QUEUE.readFrom(properties)).map(Boolean::parseBoolean).orElse(DEFAULT_QUEUE_DURABILITY);
        channel.exchangeDeclare(exchangeName, TOPIC_EXCHANGE_TYPE, durableQueue);
        return exchangeName;
    }

    private String createQueue(Channel channel, Properties properties) throws IOException {
        AMQP.Queue.DeclareOk queueDeclaration = channel.queueDeclare(QUEUE_NAME.readFrom(properties), true, false, false, new HashMap<>());
        return queueDeclaration.getQueue();
    }

    private Channel createChannel(Connection connection, Properties properties) throws IOException {
        Channel channel = connection.createChannel();
        String prefetchProperty = PREFETCH_SIZE.readFrom(properties);
        int prefetchSize = prefetchProperty == null || prefetchProperty.isEmpty() ? DEFAULT_PREFETCH_SIZE : parseInt(prefetchProperty);
        LOG.info("Prefetch size of queue is set to {}", prefetchSize);
        channel.basicQos(prefetchSize);
        return channel;
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
