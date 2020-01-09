module jflu.producer.rabbitmq {
    requires org.slf4j;
    requires jflu.core;
    requires com.rabbitmq.client;
    requires com.fasterxml.jackson.databind;
    exports be.looorent.jflu.publisher.rabbitmq;
}