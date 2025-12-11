package be.looorent.jflu.subscriber.rabbitmq.quarkus;

import io.quarkus.arc.runtime.BeanContainer;
import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;
import org.jboss.logging.Logger;

@Recorder
public class SubscriberRecorder {
    private static final Logger LOGGER = Logger.getLogger(SubscriberRecorder.class);

    private final RuntimeValue<SubscriberRuntimeConfiguration> runtimeConfig;
    private final SubscriberBuildConfiguration buildConfig;

    public SubscriberRecorder(RuntimeValue<SubscriberRuntimeConfiguration> runtimeConfig,
                              SubscriberBuildConfiguration buildConfig) {
        this.runtimeConfig = runtimeConfig;
        this.buildConfig = buildConfig;
    }

    public void configureBuild(BeanContainer container) {
        LOGGER.infof("JFLU Subscriber enabled? -> %b", buildConfig.enabled());
    }

    public void configureRuntime(BeanContainer container) {
        container.beanInstance(RabbitMQSubscriptionBootstraperProducer.class).init(runtimeConfig.getValue(), buildConfig);
    }
}
