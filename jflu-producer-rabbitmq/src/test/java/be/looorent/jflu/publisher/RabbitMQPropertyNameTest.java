package be.looorent.jflu.publisher;

import org.junit.*;
import org.junit.contrib.java.lang.system.EnvironmentVariables;

import java.util.Properties;

import static be.looorent.jflu.publisher.RabbitMQPropertyName.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
public class RabbitMQPropertyNameTest {

    private static final String DEFAULT_HOST = "localhost";
    private static final String DEFAULT_PORT = "8888";
    private static final String DEFAULT_USER = "defaultUser";
    private static final String DEFAULT_PASSWORD = "defaultPassword";
    private static final String DEFAULT_VIRTUAL_HOST = "/";
    private static final String DEFAULT_EXCHANGE_NAME = "exchange";

    @Rule
    public final EnvironmentVariables environmentVariables = new EnvironmentVariables();

    @Before
    public void setup() {
        environmentVariables.set(HOST.getEnvironmentVariableName(),          DEFAULT_HOST);
        environmentVariables.set(PORT.getEnvironmentVariableName(),          DEFAULT_PORT);
        environmentVariables.set(USERNAME.getEnvironmentVariableName(),      DEFAULT_USER);
        environmentVariables.set(PASSWORD.getEnvironmentVariableName(),      DEFAULT_PASSWORD);
        environmentVariables.set(VIRTUAL_HOST.getEnvironmentVariableName(),  DEFAULT_VIRTUAL_HOST);
        environmentVariables.set(EXCHANGE_NAME.getEnvironmentVariableName(), DEFAULT_EXCHANGE_NAME);
    }

    @Test
    public void readPropertiesFromEnvironment() {
        Properties properties = RabbitMQPropertyName.readPropertiesFromEnvironment();
        assertThat(HOST.readFrom(properties),          is(equalTo(DEFAULT_HOST)));
        assertThat(PORT.readFrom(properties),          is(equalTo(DEFAULT_PORT)));
        assertThat(USERNAME.readFrom(properties),      is(equalTo(DEFAULT_USER)));
        assertThat(PASSWORD.readFrom(properties),      is(equalTo(DEFAULT_PASSWORD)));
        assertThat(VIRTUAL_HOST.readFrom(properties),  is(equalTo(DEFAULT_VIRTUAL_HOST)));
        assertThat(EXCHANGE_NAME.readFrom(properties), is(equalTo(DEFAULT_EXCHANGE_NAME)));
    }
}
