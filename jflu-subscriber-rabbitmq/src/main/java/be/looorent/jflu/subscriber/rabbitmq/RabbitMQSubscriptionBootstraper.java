package be.looorent.jflu.subscriber.rabbitmq;

import java.util.Map;

import static be.looorent.jflu.subscriber.rabbitmq.RabbitMQSubscriptionConfiguration.fromBootstraper;
import static java.util.Collections.emptyMap;

/**
 * This classes is a proxy to create and bootstrap a {@link RabbitMQSubscriptionConfiguration}
 * @author Lorent Lempereur {@literal <lorent.lempereur.dev@gmail.com>}
 */
public class RabbitMQSubscriptionBootstraper {
    private final String username;
    private final String password;
    private final String host;
    private final Integer port;
    private final String virtualHost;
    private final String exchangeName;
    private final String queueName;
    private final Integer prefetchSize;
    private final Boolean durableQueue;
    private final Boolean waitForConnection;
    private final Boolean useSsl;

    public static RabbitMQSubscriptionBootstraper empty() {
        return new RabbitMQSubscriptionBootstraper(null, null, null, null, null, null, null, null, null, null, null);
    }

    public RabbitMQSubscriptionBootstraper(String username,
                                           String password,
                                           String host,
                                           Integer port,
                                           String virtualHost,
                                           String exchangeName,
                                           String queueName,
                                           Integer prefetchSize,
                                           Boolean durableQueue,
                                           Boolean waitForConnection,
                                           Boolean useSsl) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
        this.virtualHost = virtualHost;
        this.exchangeName = exchangeName;
        this.queueName = queueName;
        this.prefetchSize = prefetchSize;
        this.durableQueue = durableQueue;
        this.waitForConnection = waitForConnection;
        this.useSsl = useSsl;
    }

    public RabbitMQSubscriptionConfiguration bootstrap(ConsumptionExceptionHandler exceptionHandler) throws RabbitMQConnectionException {
        return fromBootstraper(this, emptyMap(), exceptionHandler);
    }

    public RabbitMQSubscriptionConfiguration bootstrap(ConsumptionExceptionHandler exceptionHandler, Map<RabbitMQPropertyName, String> overridesAttributes) throws RabbitMQConnectionException {
        return fromBootstraper(this, overridesAttributes, exceptionHandler);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

    public String getVirtualHost() {
        return virtualHost;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public String getQueueName() {
        return queueName;
    }

    public Integer getPrefetchSize() {
        return prefetchSize;
    }

    public Boolean getDurableQueue() {
        return durableQueue;
    }

    public Boolean getWaitForConnection() {
        return waitForConnection;
    }

    public Boolean getUseSsl() {
        return useSsl;
    }
}
