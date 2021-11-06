package be.looorent.jflu.publisher.rabbitmq;

import java.util.Properties;

import static java.lang.System.getenv;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;

/**
 * All properties that must be set to initialize a proper instance of {@link RabbitMQEventTopicPublisher}.
 * @author Lorent Lempereur {@literal <lorent.lempereur.dev@gmail.com>}
 */
public enum RabbitMQPropertyName {

    USERNAME("rabbitmq.username"),
    PASSWORD("rabbitmq.password"),
    HOST("rabbitmq.host"),
    PORT("rabbitmq.port"),
    VIRTUAL_HOST("rabbitmq.virtual-host"),
    EXCHANGE_NAME("rabbitmq.exchange-name"),
    EXCHANGE_DURABLE("rabbitmq.exchange-durable"),
    WAIT_FOR_CONNECTION("rabbitmq.wait-for-connection"),
    USE_SSL("rabbitmq.use-ssl")
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

    public boolean readBooleanFrom(Properties properties) {
        String textValue = readFrom(properties);
        return ofNullable(textValue)
                .map(String::trim)
                .map(String::toLowerCase)
                .filter(value -> !value.isEmpty())
                .map(Boolean::parseBoolean)
                .orElse(false);
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
}
