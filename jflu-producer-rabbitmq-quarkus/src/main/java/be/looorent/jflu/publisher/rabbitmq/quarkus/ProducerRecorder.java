package be.looorent.jflu.publisher.rabbitmq.quarkus;

import io.quarkus.arc.runtime.BeanContainer;
import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;
import org.jboss.logging.Logger;

@Recorder
public class ProducerRecorder {
    private static final Logger LOGGER = Logger.getLogger(ProducerRecorder.class);

    private final RuntimeValue<ProducerRuntimeConfiguration> runtimeConfig;
    private final ProducerBuildConfiguration buildConfig;

    public ProducerRecorder(RuntimeValue<ProducerRuntimeConfiguration> runtimeConfig,
                            ProducerBuildConfiguration buildConfig) {
        this.runtimeConfig = runtimeConfig;
        this.buildConfig = buildConfig;
    }

    public void configureBuild(BeanContainer container) {
        LOGGER.infof("JFLU Producer enabled? -> %b", buildConfig.enabled());
    }

    public void configureRuntime(BeanContainer container) {
        container.beanInstance(EventPublisherProducer.class).init(runtimeConfig.getValue(), buildConfig);
        container.beanInstance(EventFactoryProducer.class).init(runtimeConfig.getValue(), buildConfig);
    }
}
