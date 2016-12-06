package be.looorent.jflu.entity;

import be.looorent.jflu.Event;
import be.looorent.jflu.publisher.EventPublisher;
import be.looorent.jflu.publisher.PublishingException;
import com.google.common.collect.Lists;
import org.hibernate.EmptyInterceptor;
import org.hibernate.Metamodel;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.UUID.randomUUID;

/**
 * Produces events whenever an entity is subject to any CRUD operation.
 * Must be registered as session-scoped.
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
public class EntityListener extends EmptyInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(EntityListener.class);

    private final Collection<Event> events;
    private final UUID sessionId;
    private final Supplier<EventPublisher> publisher;
    private final EntityEventFactory factory;
    private final Metamodel metamodel;

    public EntityListener(Supplier<EventPublisher> publisher,
                          Supplier<Metamodel> metamodel) {
        if (publisher == null) {
            throw new IllegalArgumentException("publisher must not be null");
        }

        this.events = new ArrayList<>();
        this.publisher = publisher;
        this.metamodel = metamodel.get();
        this.factory = new EntityEventFactory();
        this.sessionId = randomUUID();
    }

    @Override
    public void afterTransactionCompletion(Transaction tx) {
        if (!tx.getRollbackOnly()) {
            EventPublisher publisher = this.publisher.get();
            try {
                for (Event event : events) {
                    publisher.publish(event);
                }
            } catch (PublishingException e) {
                LOG.error("An error occurred when publishing an event.", e);
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        events.add(factory.createEventOnDelete(entity.getClass(), id, sessionId));
    }

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        Map<String, Object> statePerProperty = new HashMap<>();
        for (int propertyIndex = 0; propertyIndex < propertyNames.length; propertyIndex++) {
            Type type = types[propertyIndex];
            if (isSimple(type)) {
                statePerProperty.put(propertyNames[propertyIndex], state[propertyIndex]);
            }
        }
        events.add(factory.createEventOnSave(entity.getClass(), id, statePerProperty, sessionId));
        return true;
    }

    private boolean isSimple(Type type) {
        return !type.isCollectionType() && !type.isAssociationType() && !type.isEntityType();
    }

    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
        Map<String, List<Object>> changePerProperty = new HashMap<>();
        for (int propertyIndex = 0; propertyIndex < propertyNames.length; propertyIndex++) {
            Type type = types[propertyIndex];
            if (isSimple(type)) {
                if (!Objects.equals(previousState[propertyIndex], currentState[propertyIndex])) {
                    changePerProperty.put(propertyNames[propertyIndex], Lists.newArrayList(previousState[propertyIndex], currentState[propertyIndex]));
                }
            }
        }
        events.add(factory.createEventOnUpdate(entity.getClass(), id, changePerProperty, sessionId));
        return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
    }
}
