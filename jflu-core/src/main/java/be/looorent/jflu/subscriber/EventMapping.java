package be.looorent.jflu.subscriber;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventMapping {


    /**
     * "" means ALL
     */
    String emitter() default "";

    EventMappingKind kind() default EventMappingKind.ALL;

    /**
     * "" means ALL
     */
    String name() default "";

    EventMappingStatus status() default EventMappingStatus.ALL;
}
