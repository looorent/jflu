package be.looorent.jflu.publisher.rabbitmq.quarkus;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigRoot;

import java.util.Optional;
import java.util.OptionalInt;

import static io.quarkus.runtime.annotations.ConfigPhase.RUN_TIME;

@ConfigRoot(name = "jflu.producer.rabbitmq", phase = RUN_TIME)
public class ProducerRuntimeConfiguration {
    @ConfigItem
    public Optional<String> username;

    @ConfigItem
    public Optional<String> password;

    @ConfigItem
    public String host;

    @ConfigItem(defaultValue = "5672")
    public OptionalInt port;

    @ConfigItem(defaultValue = "/")
    public Optional<String> virtualHost;

    @ConfigItem
    public String exchangeName;

    @ConfigItem
    public String emitter;

    @ConfigItem
    public boolean exchangeDurable;

    @ConfigItem(defaultValue = "false")
    public boolean waitForConnection;

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
                '}';
    }
}
