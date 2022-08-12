package be.looorent.jflu.subscriber.rabbitmq.quarkus.deployment;

import be.looorent.jflu.EventSerializer;
import be.looorent.jflu.*;
import be.looorent.jflu.EventSerializer.EventDataDeserializer;
import be.looorent.jflu.EventSerializer.TimestampDeserializer;
import be.looorent.jflu.entity.*;
import be.looorent.jflu.entity.EventSerializer.IdDeserializer;
import be.looorent.jflu.manual.ManualData;
import be.looorent.jflu.manual.ManualEventFactory;
import be.looorent.jflu.publisher.EventUnpublisher;
import be.looorent.jflu.request.RequestEventFactory;
import be.looorent.jflu.subscriber.*;
import be.looorent.jflu.subscriber.rabbitmq.*;
import be.looorent.jflu.subscriber.rabbitmq.quarkus.RabbitMQSubscriptionBootstraperProducer;
import be.looorent.jflu.subscriber.rabbitmq.quarkus.SubscriberBuildConfiguration;
import be.looorent.jflu.subscriber.rabbitmq.quarkus.SubscriberRecorder;
import be.looorent.jflu.subscriber.rabbitmq.quarkus.SubscriberRuntimeConfiguration;
import io.quarkus.arc.deployment.BeanContainerBuildItem;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.ServiceStartBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveClassBuildItem;
import org.jboss.logging.Logger;

import java.util.EventListener;
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
    @BuildStep
    public ServiceStartBuildItem configureSubscriber(SubscriberRecorder recorder,
                                                     SubscriberRuntimeConfiguration runtimeConfiguration,
                                                     SubscriberBuildConfiguration buildConfiguration,
                                                     BeanContainerBuildItem beanContainer) {
        LOGGER.infof("Configure Runtime of JFlu - RabbitMQ subscriber: %s", runtimeConfiguration);
        recorder.configureRuntime(runtimeConfiguration, buildConfiguration, beanContainer.getValue());
        return new ServiceStartBuildItem("");
    }

    @BuildStep(onlyIf = IsEnabled.class)
    public ReflectiveClassBuildItem configureNativeReflection() {
        LOGGER.infof("Configure Runtime of JFlu Subscriber - Native build");
        return new ReflectiveClassBuildItem(true, true,
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
                EventSerializer.class,
                EventUnpublisher.class,
                ManualEventFactory.class,
                QueueListener.class,
                RabbitMQConnectionException.class,
                RabbitMQConnectionFactory.class,
                RabbitMQExceptionHandler.class,
                RabbitMQPropertyName.class,
                RabbitMQSubscriptionBootstraper.class,
                RabbitMQSubscriptionBootstraperProducer.class,
                RabbitMQSubscriptionConfiguration.class,
                RabbitMQSubscriptionRepository.class,
                RequestEventFactory.class,
                Subscription.class,
                SubscriptionQuery.class
        );
    }

    static class IsEnabled implements BooleanSupplier {
        SubscriberBuildConfiguration configuration;

        public boolean getAsBoolean() {
            return configuration.enabled;
        }
    }
}
