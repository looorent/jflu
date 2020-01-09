package be.looorent.jflu.publisher.rabbitmq.quarkus;

import io.quarkus.arc.runtime.BeanContainer;
import io.quarkus.runtime.annotations.Recorder;
import org.jboss.logging.Logger;

@Recorder
public class ProducerRecorder {
    private static final Logger LOGGER = Logger.getLogger(ProducerRecorder.class);
    public void configureBuild(ProducerBuildConfiguration buildConfig, BeanContainer container) {
        LOGGER.infof("JFLU enabled? -> %b", buildConfig.enabled);
    }

    public void configureRuntime(ProducerRuntimeConfiguration runtimeConfig,
                                 ProducerBuildConfiguration buildConfig,
                                 BeanContainer container) {
        container.instance(EventPublisherProducer.class).init(runtimeConfig, buildConfig);
        container.instance(EventFactoryProducer.class).init(runtimeConfig, buildConfig);
    }
}
