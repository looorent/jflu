package be.looorent.jflu.subscriber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Service that reads an environment variable <code>BROKER_SUBSCRIPTION_IMPLEMENTATION</code> to instanciate a {@link BrokerSubscriptionConfiguration} using reflection.
 * All these properties will be read from environment variables.
 * The {@link BrokerSubscriptionConfiguration} implementation must provide a static method {@code createFromSystemProperties}.
 * @author Lorent Lempereur {@literal <lorent.lempereur.dev@gmail.com>}
 */
public class BrokerSubscriptionEnvironmentConfigurationProvider implements BrokerSubscriptionConfigurationProvider {

    private static final Logger LOG = LoggerFactory.getLogger(BrokerSubscriptionEnvironmentConfigurationProvider.class);
    private static final String BROKER_IMPLEMENTATION_PROPERTY = "BROKER_SUBSCRIPTION_IMPLEMENTATION";
    private static final String BROKER_FACTORY_METHOD_NAME = "createFromSystemProperties";

    @Override
    public BrokerSubscriptionConfiguration createSubscriptionConfiguration() throws BrokerException {
        String configurationClassName = readConfigurationClassName();
        try {
            LOG.debug("Instanciating Broker configuration based on environment property: {}={}", BROKER_IMPLEMENTATION_PROPERTY, configurationClassName);
            Class<? extends BrokerSubscriptionConfiguration> configurationType = (Class<? extends BrokerSubscriptionConfiguration>) Class.forName(configurationClassName);
            Method method = configurationType.getMethod(BROKER_FACTORY_METHOD_NAME);
            return (BrokerSubscriptionConfiguration) method.invoke(null);
        } catch (IllegalAccessException | NoSuchMethodException | ClassNotFoundException e) {
            LOG.error("Class does not exist to create an instance of BrokerSubcriptionConfiguration: {}. Did you define a static method called {} ? Is this class available to the classpath?",
                    configurationClassName,
                    BROKER_FACTORY_METHOD_NAME,
                    e);
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            LOG.error("An internal error occurred when creating an instance of BrokerSubcriptionConfiguration", e);
            if (e.getCause() instanceof BrokerException) {
                throw (BrokerException) e.getCause();
            }
            else {
                throw new RuntimeException(e);
            }
        }
    }

    protected String readConfigurationClassName() {
        return System.getenv(BROKER_IMPLEMENTATION_PROPERTY);
    }
}
