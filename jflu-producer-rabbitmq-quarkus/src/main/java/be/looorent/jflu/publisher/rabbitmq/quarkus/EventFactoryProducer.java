package be.looorent.jflu.publisher.rabbitmq.quarkus;

import be.looorent.jflu.manual.ManualEventFactory;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class EventFactoryProducer {
    private static final Logger LOGGER = Logger.getLogger(EventFactoryProducer.class);

    private ProducerBuildConfiguration buildConfiguration;

    @Produces
    @Dependent
    public ManualEventFactory produceManualEventFactory() {
        if (buildConfiguration.enabled) {
            LOGGER.infof("Instanciate manual event factory for emitter='%s'", buildConfiguration.emitter);
            return new ManualEventFactory(buildConfiguration.emitter);
        } else {
            return null;
        }
    }

    public void init(ProducerBuildConfiguration buildConfiguration) {
        this.buildConfiguration = buildConfiguration;
    }
}
