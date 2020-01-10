module jflu.core {
    requires org.slf4j;

    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;

    opens be.looorent.jflu to com.fasterxml.jackson.databind;
    opens be.looorent.jflu.entity to com.fasterxml.jackson.databind;
    opens be.looorent.jflu.manual to com.fasterxml.jackson.databind;
    opens be.looorent.jflu.request to com.fasterxml.jackson.databind;

    exports be.looorent.jflu;
    exports be.looorent.jflu.entity;
    exports be.looorent.jflu.manual;
    exports be.looorent.jflu.publisher;
    exports be.looorent.jflu.request;
    exports be.looorent.jflu.subscriber;
}