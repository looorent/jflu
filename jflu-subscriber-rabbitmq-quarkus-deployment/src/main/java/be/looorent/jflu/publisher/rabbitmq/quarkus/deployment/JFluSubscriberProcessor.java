package be.looorent.jflu.publisher.rabbitmq.quarkus.deployment;

import be.looorent.jflu.*;
import be.looorent.jflu.EventSerializer.EventDataDeserializer;
import be.looorent.jflu.EventSerializer.TimestampDeserializer;
import be.looorent.jflu.entity.EntityData;
import be.looorent.jflu.manual.ManualData;
import be.looorent.jflu.publisher.rabbitmq.quarkus.SubscriberBuildConfiguration;
import be.looorent.jflu.publisher.rabbitmq.quarkus.SubscriberRecorder;
import be.looorent.jflu.publisher.rabbitmq.quarkus.SubscriberRuntimeConfiguration;
import io.quarkus.arc.deployment.BeanContainerBuildItem;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveClassBuildItem;
import org.jboss.logging.Logger;

import java.util.function.BooleanSupplier;

public class JFluSubscriberProcessor {
    private static final Logger LOGGER = Logger.getLogger(JFluSubscriberProcessor.class);
    private static final String FEATURE_NAME = "jflu-subscriber";

    @BuildStep(onlyIf = IsEnabled.class)
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE_NAME);
    }

    @Record(ExecutionTime.STATIC_INIT)
    @BuildStep(onlyIf = IsEnabled.class)
    public void configureBuild(SubscriberRecorder recorder,
                               SubscriberBuildConfiguration buildConfiguration,
                               BeanContainerBuildItem beanContainer) {
        LOGGER.infof("Configure Build of JFlu - RabbitMQ subscriber: %s", buildConfiguration);
        recorder.configureBuild(buildConfiguration, beanContainer.getValue());
    }

    @Record(ExecutionTime.RUNTIME_INIT)
    @BuildStep(onlyIf = IsEnabled.class)
    public void configureProducer(SubscriberRecorder recorder,
                                  SubscriberRuntimeConfiguration runtimeConfiguration,
                                  SubscriberBuildConfiguration buildConfiguration,
                                  BeanContainerBuildItem beanContainer) {
        LOGGER.infof("Configure Runtime of JFlu - RabbitMQ subscriber: %s", runtimeConfiguration);
        recorder.configureRuntime(runtimeConfiguration, buildConfiguration, beanContainer.getValue());
    }

    @BuildStep(onlyIf = IsEnabled.class)
    public ReflectiveClassBuildItem configureNativeReflection() {
        LOGGER.infof("Configure Runtime of JFlu Subscriber - Native build");
        return new ReflectiveClassBuildItem(false, false,
                Event.class,
                EventMetadata.class,
                EventData.class,
                ManualData.class,
                EntityData.class,
                EventDataDeserializer.class,
                TimestampDeserializer.class,
                EventKind.class,
                EventStatus.class);
    }

    static class IsEnabled implements BooleanSupplier {
        SubscriberBuildConfiguration configuration;

        public boolean getAsBoolean() {
            return configuration.enabled;
        }
    }
}
