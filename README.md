# JFlu

This project aims at seamlessly creating events from an existing JVM project (without changing your codebase) in order to execute asynchronous tasks such as _event storing_, _real time analytics_, _system monitoring_,...

For now, events are generated from:
* Entity CRUD operations
* HTTP requests

This requires Java 8. However, feel free to propose any _Pull Request_ to make it compatible with Java 7 if you need it (or simply fork it for your own use).

This project is decoupled in a set of JAR you can pick up depending on your own dependency needs. Default implementation are provided for:
* Hibernate
* Spring MVC
* RabbitMQ

## Install

All librairies are available on Maven Central. For example, `jflu-subscriber-rabbitmq` can be included in your dependencies like this (Gradle example):
```groovy
compile "be.looorent:jflu-subscriber-rabbitmq:0.3"
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
BROKER_SUBSCRIPTION_IMPLEMENTATION=be.looorent.jflu.subscriber.RabbitMQSubscriptionConfiguration
```


## `jflu-producer-hibernate`

Use this JAR if you want to generate events whenever a transaction operates on Hibernate entities.

### Getting started

You can register `be.looorent.jflu.entity.EntityListener` as an Hibernate session-scoped interceptor.
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

## jflu-subscriber-rabbitmq

This JAR defines consumers to work with RabbitMQ.

Based on all `EventConsumer` defined in your codebase, this implementation register them
to a queue by binding them automatically with routing keys. This read all the valid `@EventMapping` annotation in `EventConsumer` implementations to bind them to the broker.

Note that `jflu-store` is a typical use of subscribers.

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

By default, you an use a RabbitMQ implementation by setting this environment variable:
```
BROKER_SUBSCRIPTION_IMPLEMENTATION=be.looorent.jflu.subscriber.RabbitMQSubscriptionConfiguration
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

## jflu-store

Implementation for a JVM event-store that uses PostgreSQL.
Listens for `new` (where `status=new`) events and store them into a database.

### Getting started: Deploy the event store

This implementation is Broker-agnostic. Therefore, you must provide:
* An implementation of `BrokerSubscriptionConfiguration`. _e.g._ by adding `jflu-subscriber-rabbitmq` as a JAR dependency (which is already included with `jflu-store` by default)
* `BROKER_SUBSCRIPTION_IMPLEMENTATION`: an environment variable that specifies which subclass of `BrokerSubscriptionConfiguration` to use. _e.g._ `be.looorent.jflu.subscriber.RabbitMQSubscriptionConfiguration`


For instance, this project can be deployed in a Docker container:
```
FROM cogniteev/gradle

RUN apt-get update && \
  apt-get install -y git && \
  git clone https://github.com/looorent/jflu.git

WORKDIR /data/jflu

CMD ["gradle", "jflu-store:run"]
```

This container must depend on a broker and PostgreSQL. For example, using `docker-compose`:

```
  event-store:
    build: event_store
    container_name: event-store
    env:
      - DATABASE_HOST: <xxx>
      - DATABASE_NAME: <xxx>
      - DATABASE_USERNAME: <xxx>
      - DATABASE_PORT: <xxx>
      - DATABASE_PASSWORD: <xxx>
      - RABBITMQ_USERNAME: <xxx>
      - RABBITMQ_PASSWORD: <xxx>
      - RABBITMQ_HOST: <xxx>
      - RABBITMQ_PORT: <xxx>
      - RABBITMQ_VIRTUAL_HOST: <xxx>
      - RABBITMQ_EXCHANGE_NAME: <xxx>
      - RABBITMQ_QUEUE_NAME: <xxx>
      - RABBITMQ_QUEUE_DURABLE: <xxx>
      - BROKER_SUBSCRIPTION_IMPLEMENTATION=be.looorent.jflu.subscriber.RabbitMQSubscriptionConfiguration
    links:
      - db
      - rabbitmq
```
where:
* `event_store` contains your `Dockerfile`
* `db` is a PostgreSQL serviceEventMappingKind
* `rabbitmq` is a RabbitMQ service (you can use any broker depending on the implementation you provide)

### Getting started: Replay events

To replay events from the store, use a gradle task such as:
```
gradle replay -PfirstEventId=<firstEventId>
```
where `<firstEventId>` if the store id of the first event to replay (default: 0). Therefore, you can replay events from a specific moment in time.

You can also use the `ReplayService` programmatically.

### Instanciating EventConsumers differently

If you want to create your `EventConsumer`s differently, you can override a method of `be.looorent.jflu.subscriber.SubscriptionScanner` that uses reflection by default: `protected Optional<EventConsumer> createSubscriber(Class<? extends EventConsumer> type)`

For example, if you use Guice to handle dependency injection, you can use a different implementation of `SubscriptionScanner`:

```java

```

## TODO

* Move `jflu-subscriber-rabbitmq` and `jflu-producer-rabbitmq` dependencies out of `jflu-store`'s dependencies
* Make "replay" a real gradle task
* documentation
* more tests
* Handle associations when creating entity event
