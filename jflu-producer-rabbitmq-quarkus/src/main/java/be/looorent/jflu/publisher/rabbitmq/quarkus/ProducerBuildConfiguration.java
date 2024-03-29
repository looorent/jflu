package be.looorent.jflu.publisher.rabbitmq.quarkus;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigRoot;

import static io.quarkus.runtime.annotations.ConfigPhase.BUILD_AND_RUN_TIME_FIXED;

@ConfigRoot(name = "jflu.producer.rabbitmq", phase = BUILD_AND_RUN_TIME_FIXED)
public class ProducerBuildConfiguration {

    /**
     * Produces event or not
     */
    @ConfigItem(defaultValue = "false")
    public boolean enabled;

    @Override
    public String toString() {
        return "ProducerBuildConfiguration{" +
                "enabled=" + enabled +
                '}';
    }
}
