package be.looorent.jflu.subscriber.rabbitmq.quarkus;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigRoot;

import java.util.Optional;
import java.util.OptionalInt;

import static io.quarkus.runtime.annotations.ConfigPhase.RUN_TIME;

@ConfigRoot(name = "jflu.subscriber.rabbitmq", phase = RUN_TIME)
public class SubscriberRuntimeConfiguration {
    /**
     * RabbitMQ's username
     */
    @ConfigItem
    public Optional<String> username;

    /**
     * RabbitMQ's password
     */
    @ConfigItem
    public Optional<String> password;

    /**
     * RabbitMQ's host
     */
    @ConfigItem
    public String host;

    /**
     * RabbitMQ's port
     */
    @ConfigItem(defaultValue = "5672")
    public OptionalInt port;

    /**
     * RabbitMQ's virtual host
     */
    @ConfigItem(defaultValue = "/")
    public Optional<String> virtualHost;

    /**
     * RabbitMQ's exchange name
     */
    @ConfigItem
    public String exchangeName;

    /**
     * RabbitMQ's queue name to subscribe
     */
    @ConfigItem
    public String queueName;

    /**
     * Maximum umber of events to ack at the same time
     */
    @ConfigItem(defaultValue = "10")
    public OptionalInt prefetchSize;

    /**
     * Whether the queue is durable or not
     */
    @ConfigItem(defaultValue = "true")
    public boolean durableQueue;

    /**
     * Wait for RabbitMQ to be reachable
     */
    @ConfigItem(defaultValue = "false")
    public boolean waitForConnection;

    /**
     * Use SSL with the RabbitMQ's management API or not
     */
    @ConfigItem(defaultValue = "false")
    public boolean useSsl;

    @Override
    public String toString() {
        return "SubscriberRuntimeConfiguration{" +
                "username=" + username +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", virtualHost=" + virtualHost +
                ", exchangeName='" + exchangeName + '\'' +
                ", queueName='" + queueName + '\'' +
                ", prefetchSize=" + prefetchSize +
                ", durableQueue=" + durableQueue +
                ", waitForConnection=" + waitForConnection +
                ", useSsl=" + useSsl +
                '}';
    }
}
