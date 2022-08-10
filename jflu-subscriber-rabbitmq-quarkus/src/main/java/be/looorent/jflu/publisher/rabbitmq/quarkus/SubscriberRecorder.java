package be.looorent.jflu.publisher.rabbitmq.quarkus;

import io.quarkus.arc.runtime.BeanContainer;
import io.quarkus.runtime.annotations.Recorder;
import org.jboss.logging.Logger;

@Recorder
public class SubscriberRecorder {
    private static final Logger LOGGER = Logger.getLogger(SubscriberRecorder.class);
    public void configureBuild(SubscriberBuildConfiguration buildConfig, BeanContainer container) {
        LOGGER.infof("JFLU Subscriber enabled? -> %b", buildConfig.enabled);
    }

    public void configureRuntime(SubscriberRuntimeConfiguration runtimeConfig,
                                 SubscriberBuildConfiguration buildConfig,
                                 BeanContainer container) {
    }
}
