package be.looorent.jflu.entity;

import be.looorent.jflu.Event;
import be.looorent.jflu.EventPublisher;
import org.hibernate.EmptyInterceptor;
import org.hibernate.Transaction;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Must be registered as session-scoped.
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
public class EntityListener extends EmptyInterceptor {

    private final Collection<Event> events;
    private final EventPublisher publisher;
    private final EntityEventFactory factory;

    public EntityListener(EventPublisher publisher) {
        if (publisher == null) {
            throw new IllegalArgumentException("publisher must not be null");
        }

        this.events = new ArrayList<>();
        this.publisher = publisher;
        this.factory = new EntityEventFactory();
    }

    @Override
    public void afterTransactionCompletion(Transaction tx) {
        if (!tx.getRollbackOnly()) {
            events.forEach(publisher::publish);
        }
    }

    @Override
    public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        events.add(factory.createEventOnDelete(entity));
    }

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        events.add(factory.createEventOnSave(entity));
        return true;
    }
}
