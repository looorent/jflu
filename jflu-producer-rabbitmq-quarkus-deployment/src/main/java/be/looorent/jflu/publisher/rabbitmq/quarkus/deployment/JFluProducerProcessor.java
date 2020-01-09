package be.looorent.jflu.publisher.rabbitmq.quarkus.deployment;

import be.looorent.jflu.publisher.rabbitmq.quarkus.ProducerBuildConfiguration;
import be.looorent.jflu.publisher.rabbitmq.quarkus.ProducerRecorder;
import be.looorent.jflu.publisher.rabbitmq.quarkus.ProducerRuntimeConfiguration;
import io.quarkus.arc.deployment.BeanContainerBuildItem;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import org.jboss.logging.Logger;

public class JFluProducerProcessor {
    private static final Logger LOGGER = Logger.getLogger(JFluProducerProcessor.class);

    @Record(ExecutionTime.STATIC_INIT)
    @BuildStep
    public void configureBuild(ProducerRecorder recorder,
                               ProducerBuildConfiguration buildConfiguration,
                               BeanContainerBuildItem beanContainer) {
        LOGGER.infof("Configure Build of JFLU - RabbitMQ producer: %s", buildConfiguration);
        recorder.configureBuild(buildConfiguration, beanContainer.getValue());
    }

    @Record(ExecutionTime.RUNTIME_INIT)
    @BuildStep
    public void configureProducer(ProducerRecorder recorder,
                                  ProducerRuntimeConfiguration runtimeConfiguration,
                                  ProducerBuildConfiguration buildConfiguration,
                                  BeanContainerBuildItem beanContainer) {
        LOGGER.infof("Configure Runtime of JFLU - RabbitMQ producer: %s", runtimeConfiguration);
        recorder.configureRuntime(runtimeConfiguration, buildConfiguration, beanContainer.getValue());
    }
}
