package be.looorent.jflu.subscriber;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static be.looorent.jflu.subscriber.EventMappingStatus.ALL;

/**
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventMapping {

    String emitter() default "*";

    String kind() default "*";

    String name() default "*";

    EventMappingStatus status() default ALL;
}
