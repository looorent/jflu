package be.looorent.jflu.subscriber.rabbitmq

import be.looorent.jflu.subscriber.BrokerSubscriptionConfigurationProvider
import be.looorent.jflu.subscriber.BrokerSubscriptionEnvironmentConfigurationProvider
import spock.lang.Specification

/**
 * @author Lorent Lempereur {@literal <lorent.lempereur.dev@gmail.com>}
 */
class RabbitMQBrokerSubscriptionEnvironmentConfigurationProviderTest extends Specification {

    def "loading configuration using reflection"() {
        given: "a provider"
        BrokerSubscriptionConfigurationProvider provider = new BrokerSubscriptionEnvironmentConfigurationProvider() {
            @Override
            protected String readConfigurationClassName() {
                "${RabbitMQSubscriptionConfiguration.class.package.name}.RabbitMQSubscriptionConfiguration"
            }
        }

        when: "using reflection to load a configuration"
            provider.createSubscriptionConfiguration()

        then: "an exception is thrown when connecting RabbitMQ"
            thrown RabbitMQConnectionException
    }
}
