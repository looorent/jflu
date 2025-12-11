package be.looorent.jflu.publisher.rabbitmq.quarkus;

import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

import static io.quarkus.runtime.annotations.ConfigPhase.BUILD_AND_RUN_TIME_FIXED;

@ConfigMapping(prefix = "quarkus.jflu.producer.rabbitmq")
@ConfigRoot(phase = BUILD_AND_RUN_TIME_FIXED)
public interface ProducerBuildConfiguration {

    /**
     * Produces event or not
     */
    @WithDefault("false")
    boolean enabled();
}
