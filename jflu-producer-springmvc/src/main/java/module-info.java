module jflu.producer.springmvc {
    requires org.slf4j;
    requires jflu.core;
    requires spring.webmvc;
    requires spring.web;
    requires javax.servlet.api;
    exports be.looorent.jflu.springmvc;
}