package be.looorent.jflu.subscriber;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Event's metadata to match an {@link EventConsumer} method.
 * This annotation can be used on methods of any subclass of {@link EventConsumer}?
 * By default, an {@link EventMapping} matches all events.
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventMapping {

    /**
     * "" means ALL
     * @return the event's emitter to match
     */
    String emitter() default "";

    /**
     * @return the kind of event to match
     */
    EventMappingKind kind() default EventMappingKind.ALL;

    /**
     * "" means ALL
     * @return the event's name to match
     */
    String name() default "";

    /**
     * @return the event's status to match
     */
    EventMappingStatus status() default EventMappingStatus.ALL;
}
