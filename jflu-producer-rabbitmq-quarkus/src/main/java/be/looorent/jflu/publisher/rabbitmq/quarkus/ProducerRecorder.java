package be.looorent.jflu.publisher.rabbitmq.quarkus;

import io.quarkus.runtime.annotations.Recorder;
import io.quarkus.arc.runtime.BeanContainer;

@Recorder
public class ProducerRecorder {
    public void configureBuild(ProducerBuildConfiguration buildConfig, BeanContainer container) {
        container.instance(EventFactoryProducer.class).init(buildConfig);
    }

    public void configureRuntime(ProducerRuntimeConfiguration runtimeConfig,
                                 ProducerBuildConfiguration buildConfig,
                                 BeanContainer container) {
        container.instance(EventPublisherProducer.class).init(runtimeConfig, buildConfig);
    }
}
