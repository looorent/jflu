package be.looorent.jflu.subscriber;

/**
 * Service that provides an instance of {@link BrokerSubscriptionConfiguration}.
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
public interface BrokerSubscriptionConfigurationProvider {

    /**
     * @return an instance of {@link BrokerSubscriptionConfiguration}
     */
    BrokerSubscriptionConfiguration createSubscriptionConfiguration();
}
