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
 * Scans classpath to find {@link EventConsumer}s that define consuming methods (represented by {@link Subscription}s).
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
public class SubscriptionScanner {

    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionScanner.class);

    /**
     * Scans the provided {@code packagePrefix} in the classpath to find event subscriptions.
     * This method can be very slow for large classpaths.
     * @param packagePrefix must not be null or empty
     * @return all the {@link Subscription} that can be found within these packages.
     */
    public Collection<Subscription> findAllSubscriptionsIn(String packagePrefix) {
        if (packagePrefix == null || packagePrefix.isEmpty()) {
            throw new IllegalArgumentException("packagePrefix must not be null");
        }

        Reflections reflections = new Reflections(packagePrefix);
        return reflections.getSubTypesOf(EventConsumer.class)
                .stream()
                .map(this::createSubscriber)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(this::findAllProjectionsMethods)
                .flatMap(Collection::stream)
                .collect(toList());
    }

    private Collection<Subscription> findAllProjectionsMethods(EventConsumer subscriber) {
        return stream(subscriber.getClass().getMethods())
                .filter(method -> method.isAnnotationPresent(EventMapping.class))
                .map(method -> createSubscription(subscriber, method))
                .collect(toList());
    }

    private Subscription createSubscription(EventConsumer subscriber, Method method) {
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

    private Optional<EventConsumer> createSubscriber(Class<? extends EventConsumer> type) {
        try {
            return of(type.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            LOG.error("Impossible to create an instance of this type: {}", type, e);
            return empty();
        }
    }
}
