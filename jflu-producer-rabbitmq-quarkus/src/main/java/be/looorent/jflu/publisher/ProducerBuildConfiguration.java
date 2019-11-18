package be.looorent.jflu.publisher;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigRoot;

import static io.quarkus.runtime.annotations.ConfigPhase.BUILD_AND_RUN_TIME_FIXED;

@ConfigRoot(name = "jflu.producer.rabbitmq", phase = BUILD_AND_RUN_TIME_FIXED)
public class ProducerBuildConfiguration {

    @ConfigItem
    public boolean waitForConnection;

    @ConfigItem
    public String emitter;

    @Override
    public String toString() {
        return "ProducerBuildConfiguration{" +
                "waitForConnection=" + waitForConnection +
                ", emitter='" + emitter + '\'' +
                '}';
    }
}
