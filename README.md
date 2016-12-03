# JFlu

Helpers to generate Entity CRUD Events and controller events without changing your codebase.


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
   
An environment variable must be specified:
* `JFLU_EMITTER`
    
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

An environment variable must be specified:
* `JFLU_EMITTER`

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

## jflu-store

Naïve implementation for a JVM event-store that uses `jflu-subscriber-rabbitmq` and PostgreSQL. Listens for `new` (where `status=new`) events and store them into a database.

For instance, this project can be deployed in a Docker container:
```
FROM cogniteev/gradle

RUN apt-get update && \
  apt-get install -y git && \
  git clone https://github.com/looorent/jflu.git

WORKDIR /data/jflu

CMD ["gradle", "jflu-store:run"]
```

This container must depend on RabbitMQ and PostgreSQL. For example, using `docker-compose`:

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
      - RABBITMQ_VIRTUALHOST: <xxx>
      - RABBITMQ_EXCHANGENAME: <xxx>
      - RABBITMQ_QUEUENAME: <xxx>
    links:
      - db
      - rabbitmq
```
where: 
* `event_store` contains your `Dockerfile`
* `db` is a PostgreSQL service
* `rabbitmq` is a RabbitMQ service

### TODO

* make consumer work
* do not use exclusive queue
* refactor code to make it readable
* Configure event-store to use reflection to use any implementation