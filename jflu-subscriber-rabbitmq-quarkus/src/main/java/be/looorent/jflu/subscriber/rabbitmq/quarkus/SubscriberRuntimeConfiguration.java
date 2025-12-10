package be.looorent.jflu.subscriber.rabbitmq.quarkus;

import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

import java.util.Optional;
import java.util.OptionalInt;

import static io.quarkus.runtime.annotations.ConfigPhase.RUN_TIME;

@ConfigMapping(prefix = "quarkus.jflu.subscriber.rabbitmq")
@ConfigRoot(phase = RUN_TIME)
public interface SubscriberRuntimeConfiguration {
    /**
     * RabbitMQ's username
     */
    Optional<String> username();

    /**
     * RabbitMQ's password
     */
    Optional<String> password();

    /**
     * RabbitMQ's host
     */
    String host();

    /**
     * RabbitMQ's port
     */
    @WithDefault("5672")
    OptionalInt port();

    /**
     * RabbitMQ's virtual host
     */
    @WithDefault("/")
    Optional<String> virtualHost();

    /**
     * RabbitMQ's exchange name
     */
    String exchangeName();

    /**
     * RabbitMQ's queue name to subscribe
     */
    String queueName();

    /**
     * Maximum umber of events to ack at the same time
     */
    @WithDefault("10")
    OptionalInt prefetchSize();

    /**
     * Whether the queue is durable or not
     */
    @WithDefault("true")
    boolean durableQueue();

    /**
     * Wait for RabbitMQ to be reachable
     */
    @WithDefault("false")
    boolean waitForConnection();

    /**
     * Use SSL with the RabbitMQ's management API or not
     */
    @WithDefault("false")
    boolean useSsl();
}
