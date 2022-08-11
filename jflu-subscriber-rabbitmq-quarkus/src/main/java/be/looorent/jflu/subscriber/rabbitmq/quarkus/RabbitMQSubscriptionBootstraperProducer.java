package be.looorent.jflu.subscriber.rabbitmq.quarkus;

import be.looorent.jflu.subscriber.rabbitmq.RabbitMQSubscriptionBootstraper;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class RabbitMQSubscriptionBootstraperProducer {
    private static final Logger LOGGER = Logger.getLogger(RabbitMQSubscriptionBootstraperProducer.class);

    private SubscriberRuntimeConfiguration runtimeConfiguration;
    private SubscriberBuildConfiguration buildConfiguration;

    @Produces
    @Dependent
    public RabbitMQSubscriptionBootstraper produceRabbitMQSubscriptionBootstraper() {
        if (isEnabled()) {
            LOGGER.infof("Instanciate RabbitMQ Subscription bootstraper");
            return new RabbitMQSubscriptionBootstraper(
                    runtimeConfiguration.username.orElse(null),
                    runtimeConfiguration.password.orElse(null),
                    runtimeConfiguration.host,
                    runtimeConfiguration.port.orElse(5672),
                    runtimeConfiguration.virtualHost.orElse(null),
                    runtimeConfiguration.exchangeName,
                    runtimeConfiguration.queueName,
                    runtimeConfiguration.prefetchSize.orElse(10),
                    runtimeConfiguration.durableQueue,
                    runtimeConfiguration.waitForConnection,
                    runtimeConfiguration.useSsl
            );
        } else {
            LOGGER.info("JFlu-Subscriber is disabled. Using an empty bootstraper");
            return RabbitMQSubscriptionBootstraper.empty();
        }
    }

    public void init(SubscriberRuntimeConfiguration runtimeConfiguration, SubscriberBuildConfiguration buildConfiguration) {
        this.runtimeConfiguration = runtimeConfiguration;
        this.buildConfiguration = buildConfiguration;
    }

    private boolean isEnabled() {
        return buildConfiguration != null && buildConfiguration.enabled;
    }
}
