package be.looorent.jflu.subscriber

import spock.lang.Specification

/**
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
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
