package be.looorent.jflu.publisher.rabbitmq.quarkus;

import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

import java.util.Optional;
import java.util.OptionalInt;

import static io.quarkus.runtime.annotations.ConfigPhase.RUN_TIME;

@ConfigMapping(prefix = "quarkus.jflu.producer.rabbitmq")
@ConfigRoot(phase = RUN_TIME)
public interface ProducerRuntimeConfiguration {
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
     * Name of emitter for each event
     */
    String emitter();

    /**
     * Whether the event must be flagged with "exchange durable" or not
     */
    boolean exchangeDurable();

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
