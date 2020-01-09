module jflu.subscriber.store {
    requires org.slf4j;
    requires liquibase.core;
    requires postgresql;
    requires logback.core;
    requires logback.classic;
    requires java.sql;
    requires com.google.common;
    requires com.fasterxml.jackson.databind;
    requires jflu.core;
    requires jflu.subscriber.rabbitmq;
    requires jflu.subscriber.reflection;
    requires jflu.producer.rabbitmq;
    exports be.looorent.jflu.store;
}