package be.looorent.jflu.subscriber;

import be.looorent.jflu.Configuration;
import be.looorent.jflu.Event;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static be.looorent.jflu.subscriber.SubscriptionQuery.exactMatchWith;
import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toList;

/**
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
public class SubscriptionRepository {

    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionRepository.class);

    private final ObjectMapper jsonMapper;
    private final ConcurrentMap<SubscriptionQuery, List<Subscription>> subscriptionsByQuery;

    public SubscriptionRepository() {
        this.jsonMapper = Configuration.getInstance().getDefaultJsonMapper();
        this.subscriptionsByQuery = new ConcurrentHashMap<>();
    }

    public void register(Subscription subscription) {
        LOG.info("Register subscription for mapping : {}", subscription.getQuery());
        List<Subscription> subscriptions = subscriptionsByQuery.get(subscription);
        if (subscriptions == null) {
            subscriptions = new ArrayList<>();
            subscriptionsByQuery.put(subscription.getQuery(), subscriptions);
        }
        subscriptions.add(subscription);
    }

    public Collection<Subscription> findAllSubscriptionsFor(Event event) {
        return SubscriptionQuery.allPossibilitiesFor(event)
                .stream()
                .flatMap(query -> subscriptionsByQuery.getOrDefault(query, new ArrayList<>()).stream())
                .collect(toList());
    }

    public ObjectMapper getJsonMapper() {
        return jsonMapper;
    }
}
