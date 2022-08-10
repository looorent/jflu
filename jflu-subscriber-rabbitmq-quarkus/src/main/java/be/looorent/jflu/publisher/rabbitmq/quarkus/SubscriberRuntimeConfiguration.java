package be.looorent.jflu.publisher.rabbitmq.quarkus;

import io.quarkus.runtime.annotations.ConfigRoot;

import static io.quarkus.runtime.annotations.ConfigPhase.RUN_TIME;

@ConfigRoot(name = "jflu.subscriber.rabbitmq", phase = RUN_TIME)
public class SubscriberRuntimeConfiguration {
}
