package be.looorent.jflu.subscriber;

/**
 * Service that provides an instance of {@link BrokerSubscriptionConfiguration}.
 * @author Lorent Lempereur {@literal <lorent.lempereur.dev@gmail.com>}
 */
public interface BrokerSubscriptionConfigurationProvider {

    /**
     * @return an instance of {@link BrokerSubscriptionConfiguration}
     * @throws BrokerException if a connection error occurs
     */
    BrokerSubscriptionConfiguration createSubscriptionConfiguration() throws BrokerException;
}
