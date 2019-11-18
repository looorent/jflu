package be.looorent.jflu.publisher;

import be.looorent.jflu.manual.ManualEventFactory;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import java.util.Properties;

import static be.looorent.jflu.publisher.RabbitMQPropertyName.*;

@ApplicationScoped
public class EventFactoryProducer {
    private static final Logger LOGGER = Logger.getLogger(EventFactoryProducer.class);

    private ProducerBuildConfiguration buildConfiguration;

    @Produces
    @Dependent
    public ManualEventFactory produceManualEventFactory() {
        LOGGER.infof("Instanciate manual event factory for emitter='%s'", buildConfiguration.emitter);
        return new ManualEventFactory(buildConfiguration.emitter);
    }

    public void init(ProducerBuildConfiguration buildConfiguration) {
        this.buildConfiguration = buildConfiguration;
    }
}
