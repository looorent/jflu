package be.looorent.jflu.publisher.rabbitmq.quarkus;

import io.quarkus.arc.runtime.BeanContainer;
import io.quarkus.runtime.annotations.Recorder;
import org.jboss.logging.Logger;

@Recorder
public class ProducerRecorder {
    private static final Logger LOGGER = Logger.getLogger(ProducerRecorder.class);
    public void configureBuild(ProducerBuildConfiguration buildConfig, BeanContainer container) {
        LOGGER.infof("JFLU Producer enabled? -> %b", buildConfig.enabled);
    }

    public void configureRuntime(ProducerRuntimeConfiguration runtimeConfig,
                                 ProducerBuildConfiguration buildConfig,
                                 BeanContainer container) {
        container.beanInstance(EventPublisherProducer.class).init(runtimeConfig, buildConfig);
        container.beanInstance(EventFactoryProducer.class).init(runtimeConfig, buildConfig);
    }
}
