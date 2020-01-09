package be.looorent.jflu.publisher.rabbitmq.quarkus.deployment;

import be.looorent.jflu.publisher.rabbitmq.quarkus.ProducerBuildConfiguration;
import be.looorent.jflu.publisher.rabbitmq.quarkus.ProducerRecorder;
import be.looorent.jflu.publisher.rabbitmq.quarkus.ProducerRuntimeConfiguration;
import io.quarkus.arc.deployment.BeanContainerBuildItem;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import org.jboss.logging.Logger;

import java.util.function.BooleanSupplier;

public class JFluProducerProcessor {
    private static final Logger LOGGER = Logger.getLogger(JFluProducerProcessor.class);
    private static final String FEATURE_NAME = "jflu";

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

    static class IsEnabled implements BooleanSupplier {
        ProducerBuildConfiguration configuration;

        public boolean getAsBoolean() {
            return configuration.enabled;
        }
    }
}
