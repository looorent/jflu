package be.looorent.jflu.publisher.rabbitmq.quarkus;

import be.looorent.jflu.manual.ManualEventFactory;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class EventFactoryProducer {
    private static final Logger LOGGER = Logger.getLogger(EventFactoryProducer.class);

    private ProducerRuntimeConfiguration runtimeConfiguration;
    private ProducerBuildConfiguration buildConfiguration;

    @Produces
    @Dependent
    public ManualEventFactory produceManualEventFactory() {
        String emitter = isEnabled() ? runtimeConfiguration.emitter : "[JFluDisabled]";
        LOGGER.infof("Instanciate manual event factory for emitter='%s'", emitter);
        return new ManualEventFactory(emitter);
    }

    public void init(ProducerRuntimeConfiguration runtimeConfiguration, ProducerBuildConfiguration buildConfiguration) {
        this.runtimeConfiguration = runtimeConfiguration;
        this.buildConfiguration = buildConfiguration;
    }

    private boolean isEnabled() {
        return buildConfiguration != null && buildConfiguration.enabled;
    }
}
