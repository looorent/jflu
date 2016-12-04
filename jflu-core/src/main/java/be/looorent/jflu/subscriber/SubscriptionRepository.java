package be.looorent.jflu.subscriber;

import be.looorent.jflu.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static java.util.stream.Collectors.toList;

/**
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
public class SubscriptionRepository {

    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionRepository.class);

    private final ConcurrentMap<SubscriptionQuery, List<Subscription>> subscriptionsByQuery;

    public SubscriptionRepository() {
        this.subscriptionsByQuery = new ConcurrentHashMap<>();
    }

    /**
     * Register a new subscription to consume events.
     * @param subscription the subscription to register; must not be null;
     */
    public void register(Subscription subscription) {
        if (subscription == null) {
            throw new IllegalArgumentException("subscription must not be null");
        }

        LOG.info("Register subscription for mapping : {}", subscription.getName());
        subscriptionsByQuery.putIfAbsent(subscription.getQuery(), new ArrayList<>());
        subscriptionsByQuery.get(subscription).add(subscription);
    }

    /**
     * Find all subscriptions whose the {@link SubscriptionQuery} matches the provided event
     * @param event can be null (will return an empty list)
     * @return all the matching subscriptions
     */
    public Collection<Subscription> findAllSubscriptionsFor(Event event) {
        return event == null ? new ArrayList<>() :
                SubscriptionQuery.allQueriesThatMatch(event)
                .stream()
                .flatMap(query -> subscriptionsByQuery.getOrDefault(query, new ArrayList<>()).stream())
                .collect(toList());
    }
}
