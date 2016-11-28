package be.looorent.jflu.subscriber;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Optional;

import static java.util.Arrays.stream;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;

/**
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
public class SubscriptionScanner {

    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionScanner.class);

    public Collection<Subscription> findAllSubscriptions(String rootPackage) {
        if (rootPackage == null || rootPackage.isEmpty()) {
            throw new IllegalArgumentException("rootPackage must not be null");
        }

        Reflections reflections = new Reflections(rootPackage);
        return reflections.getSubTypesOf(EventSubscriber.class)
                .stream()
                .map(this::createSubscriber)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(this::findAllProjectionsMethods)
                .flatMap(Collection::stream)
                .collect(toList());
    }

    private Collection<Subscription> findAllProjectionsMethods(EventSubscriber subscriber) {
        return stream(subscriber.getClass().getMethods())
                .filter(method -> method.isAnnotationPresent(EventMapping.class))
                .map(method -> createSubscription(subscriber, method))
                .collect(toList());
    }

    private Subscription createSubscription(EventSubscriber subscriber, Method method) {
        String name = method.getDeclaringClass().getSimpleName() + "." + method.getName();
        SubscriptionQuery query = new SubscriptionQuery(method.getAnnotation(EventMapping.class));
        return new Subscription(query, name, event -> {
            try {
                method.invoke(subscriber, event);
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOG.error("This subscriber cannot be called.");
            } catch (Exception e) {
                LOG.error("An error occurred when processing this event: {}", event.getId(), e);
                throw new IllegalArgumentException(e);
            }
        });
    }

    private Optional<EventSubscriber> createSubscriber(Class<? extends EventSubscriber> type) {
        try {
            return of(type.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            LOG.error("Impossible to create an instance of this type: {}", type, e);
            return empty();
        }
    }
}
