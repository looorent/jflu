package be.looorent.jflu.subscriber.rabbitmq;

import java.util.Map;
import java.util.Properties;

import static java.lang.System.getenv;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;

/**
 * All properties that must be set to initialize a proper instance of all RabbitMQ implementations.
 * @author Lorent Lempereur {@literal <lorent.lempereur.dev@gmail.com>}
 */
public enum RabbitMQPropertyName {

    USERNAME("rabbitmq.username"),
    PASSWORD("rabbitmq.password"),
    HOST("rabbitmq.host"),
    PORT("rabbitmq.port"),
    VIRTUAL_HOST("rabbitmq.virtual-host"),
    EXCHANGE_NAME("rabbitmq.exchange-name"),
    QUEUE_NAME("rabbitmq.queue-name"),
    PREFETCH_SIZE("rabbitmq.prefetch-size"),
    DURABLE_QUEUE("rabbitmq.queue-durable"),
    WAIT_FOR_CONNECTION("rabbitmq.wait-for-connection")
    ;

    private final String propertyName;

    RabbitMQPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String readFrom(Properties properties) {
        if (properties == null) {
            throw new IllegalArgumentException("properties must not be null");
        }
        return properties.getProperty(propertyName);
    }

    public void writeTo(Properties properties, Object value) {
        if (properties == null) {
            throw new IllegalArgumentException("properties must not be null");
        }
        properties.setProperty(propertyName, String.valueOf(value));
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getEnvironmentVariableName() {
        return propertyName.toUpperCase().replace("-", "_").replace(".", "_");
    }

    public static final Properties readPropertiesFromEnvironment() {
        Properties properties = new Properties();
        properties.putAll(stream(values())
                .collect(toMap(RabbitMQPropertyName::getPropertyName,
                               property -> {
                                    String value = getenv(property.getEnvironmentVariableName());
                                    return value == null ? "" : value;
                               })));
        return properties;
    }

    public static final Properties merge(Properties properties, Map<RabbitMQPropertyName, String> otherProperties) {
        Properties merged = new Properties(properties);
        if (otherProperties != null) {
            for (Map.Entry<RabbitMQPropertyName, String> keyAndValue : otherProperties.entrySet()) {
                merged.setProperty(keyAndValue.getKey().getPropertyName(), keyAndValue.getValue());
            }
        }
        return merged;
    }
}
