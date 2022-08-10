package be.looorent.jflu.publisher.rabbitmq.quarkus;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigRoot;

import java.util.Optional;
import java.util.OptionalInt;

import static io.quarkus.runtime.annotations.ConfigPhase.RUN_TIME;

@ConfigRoot(name = "jflu.producer.rabbitmq", phase = RUN_TIME)
public class ProducerRuntimeConfiguration {
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
     * Name of emitter for each event
     */
    @ConfigItem
    public String emitter;

    /**
     * Whether the event must be flagged with "exchange durable" or not
     */
    @ConfigItem
    public boolean exchangeDurable;

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
        return "ProducerRuntimeConfiguration{" +
                "username=" + username +
                ", password=" + password +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", virtualHost=" + virtualHost +
                ", exchangeName='" + exchangeName + '\'' +
                ", emitter='" + emitter + '\'' +
                ", exchangeDurable=" + exchangeDurable +
                ", waitForConnection=" + waitForConnection +
                ", useSsl=" + useSsl +
                '}';
    }
}
