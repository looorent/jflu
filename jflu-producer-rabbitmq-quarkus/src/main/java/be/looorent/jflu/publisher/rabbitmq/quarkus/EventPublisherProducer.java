package be.looorent.jflu.publisher.rabbitmq.quarkus;

import be.looorent.jflu.publisher.EventPublisher;
import be.looorent.jflu.publisher.EventUnpublisher;
import be.looorent.jflu.publisher.rabbitmq.RabbitMQEventTopicPublisher;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import java.util.Properties;

import static be.looorent.jflu.publisher.rabbitmq.RabbitMQPropertyName.*;

@ApplicationScoped
public class EventPublisherProducer {
    private static final Logger LOGGER = Logger.getLogger(EventPublisherProducer.class);

    private ProducerRuntimeConfiguration runtimeConfiguration;
    private ProducerBuildConfiguration buildConfiguration;

    @Produces
    @Dependent
    public EventPublisher producePublisher() {
        if (isEnabled()) {
            LOGGER.info("Instanciate singleton EventPublisher of type RabbitMQEventTopicPublisher");
            Properties properties = new Properties();
            USERNAME.writeTo(properties, runtimeConfiguration.username.get());
            PASSWORD.writeTo(properties, runtimeConfiguration.password.get());
            HOST.writeTo(properties, runtimeConfiguration.host);
            PORT.writeTo(properties, runtimeConfiguration.port.orElse(0));
            VIRTUAL_HOST.writeTo(properties, runtimeConfiguration.virtualHost.get());
            EXCHANGE_NAME.writeTo(properties, runtimeConfiguration.exchangeName);
            EXCHANGE_DURABLE.writeTo(properties, runtimeConfiguration.exchangeDurable);
            WAIT_FOR_CONNECTION.writeTo(properties, runtimeConfiguration.waitForConnection);
            USE_SSL.writeTo(properties, runtimeConfiguration.useSsl);
            return new RabbitMQEventTopicPublisher(properties);
        } else {
            LOGGER.info("JFlu is disabled. EventPublisher will not publish anywhere.");
            return new EventUnpublisher();
        }
    }

    public void init(ProducerRuntimeConfiguration runtimeConfiguration, ProducerBuildConfiguration buildConfiguration) {
        this.runtimeConfiguration = runtimeConfiguration;
        this.buildConfiguration = buildConfiguration;
    }

    private boolean isEnabled() {
        return buildConfiguration != null && buildConfiguration.enabled;
    }
}
