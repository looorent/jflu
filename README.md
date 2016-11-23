# JFlu

Helpers to generate Entity CRUD Events and controller events without changing your codebase.

## Prerequisites

* Using Hibernate 
* Pouet pouet

## Configuration

A system property must be specified:
* `JFLU_EMITTER`


DRAAAAFT

## `jflu-core`

This JAR defines base classes to work with events and factories.
These classes also define all structures and how they must be un/marshalled from/to JSON.

## `jflu-producer-hibernate`

Use this JAR if you want to generate events whenever a transaction operates on Hibernate entities.

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
* Register this provider as an Hibernate session-scoped interceptor.
    ```
    hibernate.ejb.interceptor.session_scoped: FluEntityInterceptor
    ```
    
## `jflu-producer-springmvc`

Use this JAR if you want to generate events whenever a Spring MVC controller is called.

For instance, register an instance of `` (which requires an implementation of `EventPublisher`) as a Spring MVC interceptor handler.
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

## jflu-producer-rabbitmq

This JAR defines an `EventPublisher` that work with RabbitMQ: `RabbitMQEventPublisher`.

This implementation is based on the RabbitMQ Topic model.

To initialize an instance of `RabbitMQEventPublisher`, several properties must be provided:
* `rabbitmq.username`
* `rabbitmq.password`
* `rabbitmq.virtualHost`
* `rabbitmq.host`
* `rabbitmq.port`
* `rabbitmq.exchangeName`

## jflu-subscriber-rabbitmq

This JAR defines consumers and projectors to work with RabbitMQ.


