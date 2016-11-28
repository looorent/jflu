package be.looorent.jflu.store;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static java.lang.System.getenv;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;

/**
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
public enum EventStoreDatabaseConfiguration {

    HOST("database.host"),
    NAME("database.name"),
    USERNAME("database.username"),
    PORT("database.port"),
    PASSWORD("database.password")
    ;

    private final String propertyName;

    EventStoreDatabaseConfiguration(String propertyName) {
        this.propertyName = propertyName;
    }

    public String readFrom(Properties properties) {
        if (properties == null) {
            throw new IllegalArgumentException("properties must not be null");
        }
        return properties.getProperty(propertyName);
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getEnvironmentVariableName() {
        return propertyName.toUpperCase().replace(".", "_");
    }

    public static final Properties readPropertiesFromEnvironment() {
        Properties properties = new Properties();
        properties.putAll(stream(values())
                .collect(toMap(EventStoreDatabaseConfiguration::getPropertyName,
                               property -> {
                                    String value = getenv(property.getEnvironmentVariableName());
                                    return value == null ? "" : value;
                               })));
        return properties;
    }

    public static Connection createDatabaseConnection() throws SQLException {
        Properties properties = readPropertiesFromEnvironment();
        String url = "jdbc:postgresql://"
                + HOST.readFrom(properties)
                + ":"
                + PORT.readFrom(properties)
                + "/"
                + NAME.readFrom(properties);
        return DriverManager.getConnection(url,
                USERNAME.readFrom(properties),
                PASSWORD.readFrom(properties));
    }
}
