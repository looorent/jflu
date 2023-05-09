package be.looorent.jflu.publisher.rabbitmq.quarkus.deployment;

import be.looorent.jflu.EventSerializer;
import be.looorent.jflu.*;
import be.looorent.jflu.EventSerializer.EventDataDeserializer;
import be.looorent.jflu.EventSerializer.TimestampDeserializer;
import be.looorent.jflu.entity.*;
import be.looorent.jflu.entity.EventSerializer.IdDeserializer;
import be.looorent.jflu.manual.ManualData;
import be.looorent.jflu.manual.ManualEventFactory;
import be.looorent.jflu.publisher.EventUnpublisher;
import be.looorent.jflu.publisher.rabbitmq.RabbitMQConnectionFactory;
import be.looorent.jflu.publisher.rabbitmq.RabbitMQPropertyName;
import be.looorent.jflu.publisher.rabbitmq.quarkus.ProducerBuildConfiguration;
import be.looorent.jflu.publisher.rabbitmq.quarkus.ProducerRecorder;
import be.looorent.jflu.publisher.rabbitmq.quarkus.ProducerRuntimeConfiguration;
import be.looorent.jflu.request.RequestEventFactory;
import be.looorent.jflu.subscriber.*;
import io.quarkus.arc.deployment.BeanContainerBuildItem;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveClassBuildItem;
import org.jboss.logging.Logger;

import java.util.EventListener;
import java.util.function.BooleanSupplier;

public class JFluProducerProcessor {
    private static final Logger LOGGER = Logger.getLogger(JFluProducerProcessor.class);
    private static final String FEATURE_NAME = "jflu-producer";

    @BuildStep(onlyIf = IsEnabled.class)
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE_NAME);
    }

    @Record(ExecutionTime.STATIC_INIT)
    @BuildStep(onlyIf = IsEnabled.class)
    public void configureBuild(ProducerRecorder recorder,
                               ProducerBuildConfiguration buildConfiguration,
                               BeanContainerBuildItem beanContainer) {
        LOGGER.infof("Configure Build of JFlu - RabbitMQ producer: %s", buildConfiguration);
        recorder.configureBuild(buildConfiguration, beanContainer.getValue());
    }

    @Record(ExecutionTime.RUNTIME_INIT)
    @BuildStep(onlyIf = IsEnabled.class)
    public void configureProducer(ProducerRecorder recorder,
                                  ProducerRuntimeConfiguration runtimeConfiguration,
                                  ProducerBuildConfiguration buildConfiguration,
                                  BeanContainerBuildItem beanContainer) {
        LOGGER.infof("Configure Runtime of JFlu - RabbitMQ producer: %s", runtimeConfiguration);
        recorder.configureRuntime(runtimeConfiguration, buildConfiguration, beanContainer.getValue());
    }

    @BuildStep(onlyIf = IsEnabled.class)
    public ReflectiveClassBuildItem configureNativeReflection() {
        LOGGER.infof("Configure Runtime of JFlu Producer - Native build");
        return ReflectiveClassBuildItem.builder(
                Event.class,
                EventMetadata.class,
                EventData.class,
                ManualData.class,
                EntityData.class,
                EventDataDeserializer.class,
                TimestampDeserializer.class,
                IdDeserializer.class,
                Payload.class,
                EventKind.class,
                EventStatus.class,
                EntityActionName.class,
                EntityChange.class,

                // below this line, not sure these are required
                Auditable.class,
                BrokerSubscriptionConfiguration.class,
                BrokerSubscriptionConfigurationProvider.class,
                ConsumptionException.class,
                EntityData.class,
                EntityEventFactory.class,
                EventListener.class,
                EventMappingKind.class,
                EventSerializer.class,
                EventUnpublisher.class,
                ManualEventFactory.class,
                QueueListener.class,
                RabbitMQConnectionFactory.class,
                RabbitMQPropertyName.class,
                RequestEventFactory.class,
                Subscription.class,
                SubscriptionQuery.class
        ).methods(true).fields(true).build();
    }

    static class IsEnabled implements BooleanSupplier {
        ProducerBuildConfiguration configuration;

        public boolean getAsBoolean() {
            return configuration.enabled;
        }
    }
}
