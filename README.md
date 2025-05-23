# JFlu

This project aims at seamlessly creating events from an existing JVM project (without changing your codebase) in order to execute asynchronous tasks such as _event storing_, _real time analytics_, _system monitoring_,...

For now, events are generated from:
* Entity CRUD operations
* HTTP requests


This project is decoupled in a set of JAR you can pick up depending on your own dependency needs. Default implementation are provided for:
* Hibernate
* Spring MVC
* RabbitMQ

## Install

All librairies are available on Maven Central. For example, `jflu-subscriber-rabbitmq` can be included in your dependencies like this (Gradle example):
```groovy
implementation "be.looorent:jflu-subscriber-rabbitmq:3.0.3"
```

## `jflu-core`

This JAR defines:
* base classes to work with events, producers and subscribers;
* structures and how they must be un/marshalled from/to JSON;
* all interfaces that require a specific implementation based on your own system architecture and technologies.

Most of the default implementations can be extended by composition or inheritance.

### Getting started: Emitting events

To emit events, you must refer to dedicated implementations, depending on your own architecture and technologies.

### Getting started: Consuming events

Even if a subscription implementation is required to connect a broker and consume its messages, consumers can be defined regardless the broker implementation.

#### Define Consumers

To subscribe the broker and consume its messages, define a set of `EventConsumer` implementations. All their methods will be registered and will consume events if they are annotated with `@EventMapping`. For instance:

```
class StandardOutputConsumer implements EventConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(StandardOutputConsumer.class);

    public StandardOutputConsumer() {}

    @EventMapping(status = NEW, kind = ENTITY_CHANGE)
    public void logEntityChange(Event event) {
        LOG.info("An entity has changed and generated an event with id={}", event.getId());
    }

    @EventMapping(status = NEW, kind = REQUEST)
    public void logRequest(Event event) {
        LOG.info("A controller has been called and generated an event with id={}", event.getId());
    }
}
```

Note that:
* a default constructor is required;
* multiple consumers can be registered for the same mapping.

#### Start these consumers

In order to start these consumers, `EventListener.listenTo` must be called using two implementations:
* `SubscriptionRepository`: this default implementation can be extended depending on the broker you use (however, this class is a perfectly-working implementation)
* `QueueListener`: defines how to consume messages from a queue

These two implementations can be provided by an instance of `BrokerSubscriptionConfiguration` (which must contain your broker's specific logic).
Any implementation of `BrokerSubscriptionConfigurationProvider` can provide an instance of `BrokerSubscriptionConfiguration`.
`jflu-core` provides a default implementation that read properties from Environment Variables (`BrokerSubscriptionEnvironmentConfigurationProvider`) but feel free to add your own.

This default implementation reads an environment variable `BROKER_SUBSCRIPTION_IMPLEMENTATION` to know which `BrokerSubscriptionConfiguration` implementation it must use. The JAR `jflu-subscriber-rabbitmq` contains a implementation for RabbitMQ. Using this one, you must define the environment variable:
```
BROKER_SUBSCRIPTION_IMPLEMENTATION=be.looorent.jflu.subscriber.rabbitmq.RabbitMQSubscriptionConfiguration
```


## `jflu-producer-hibernate`

Use this JAR if you want to generate events whenever a transaction operates on Hibernate entities.

### Getting started

You can register `be.looorent.jflu.hibernate.EntityListener` as an Hibernate session-scoped interceptor.
`EntityListener` requires an instance of `EventPublisher`.

For instance:
* Create a subclass that provides an `EventPublisher` supplier.
    ```
    class FluEntityInterceptor extends EntityListener {

        FluEntityInterceptor() {
            super(() -> new YourEventPublisher())
        }
    }
    ```
* Register this provider as an Hibernate *session-scoped* interceptor.
    ```
    hibernate.ejb.interceptor.session_scoped: FluEntityInterceptor
    ```

An environment variable must be specified to set the events' emitter: `JFLU_EMITTER`. If no `JFLU_EMITTER` environment variable is set, `[not emitter set]` will be used as the default emitter.

## `jflu-producer-springmvc`

Use this JAR if you want to generate events whenever a Spring MVC controller is called.

### Getting started

For instance, register an instance of `WebMvcConfigurerAdapter` (which requires an implementation of `EventPublisher`) as a Spring MVC interceptor handler.
```
@Configuration
class FluRequestInterceptor extends WebMvcConfigurerAdapter {

    @Autowired
    private EventPublisher publisher

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestListener(publisher))
    }
}
```

An environment variable must be specified to set the events' emitter: `JFLU_EMITTER`. If no `JFLU_EMITTER` environment variable is set, `[not emitter set]` will be used as the default emitter.

## jflu-producer-rabbitmq

This JAR defines an `EventPublisher` that works with RabbitMQ: `RabbitMQEventPublisher`. All events will be sent to a RabbitMQ exchange using routing keys (see `be.looorent.jflu.RoutingKeyBuilder`).
This implementation is based on the RabbitMQ Topic model.

### Getting started

To initialize an instance of `RabbitMQEventPublisher`, several properties must be provided:
* `rabbitmq.username`
* `rabbitmq.password`
* `rabbitmq.virtual-host`
* `rabbitmq.host`
* `rabbitmq.port`
* `rabbitmq.exchange-name`
* `rabbitmq.exchange-durable`
* `rabbitmq.wait-for-connection`
* `rabbitmq.use-ssl`

## jflu-subscriber-rabbitmq

This JAR defines consumers to work with RabbitMQ.

Based on all `EventConsumer` defined in your codebase, this implementation register them
to a queue by binding them automatically with routing keys. This read all the valid `@EventMapping` annotation in `EventConsumer` implementations to bind them to the broker.

### Getting started

To initialize the subscription configuration, several properties must be provided (they are read from environment variables automatically):

* `rabbitmq.username`
* `rabbitmq.password`
* `rabbitmq.host`
* `rabbitmq.port`
* `rabbitmq.virtual-host`
* `rabbitmq.exchange-name`
* `rabbitmq.queue-name`
* `rabbitmq.prefetch-size`
* `rabbitmq.queue-durable`
* `rabbitmq.wait-for-connection`
* `rabbitmq.use-ssl`

By default, you can use a RabbitMQ implementation by setting this environment variable:
```
BROKER_SUBSCRIPTION_IMPLEMENTATION=be.looorent.jflu.subscriber.rabbitmq.RabbitMQSubscriptionConfiguration
```

Then, this line of code will return a RabbitMQ configuration:
```
BrokerSubscriptionConfiguration configuration = new BrokerSubscriptionEnvironmentConfigurationProvider().createSubscriptionConfiguration();
```

Therefore, you can start your projectors using this code:
```java
BrokerSubscriptionConfiguration configuration = new BrokerSubscriptionEnvironmentConfigurationProvider().createSubscriptionConfiguration();
new EventListener().start("your.package.with.projectors",
        new SubscriptionScanner(),
        configuration.getSubscriptionRepository(),
        configuration.getQueueListener());
```

### Custom handling of ConsumptionException

By default, when a `ConsumptionException` is raised, the broker will wrap the exception in a `RuntimeException` then interrupt the consumer thread.
If you want to handle this exception yourself, you can create your own implementation by extending the interface `ConsumptionExceptionHandler`, then add the following environment variable:
```
CONSUMPTION_EXCEPTION_HANDLER_IMPLEMENTATION=your.custom.ExceptionHandler
```
This class must be instantiable.

## Release

```shell
./gradlew build 
./gradlew jreleaserConfig
./gradlew clean
./gradlew publish
./gradlew jreleaserFullRelease
```

## Quarkus support

Support:
* the RabbitMQ Producer
* the RabbitMQ Subscriber
