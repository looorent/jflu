package be.looorent.jflu.subscriber

import be.looorent.jflu.Event
import be.looorent.jflu.EventKind
import be.looorent.jflu.EventStatus
import spock.lang.Specification

/**
 * @author Lorent Lempereur {@literal <lorent.lempereur.dev@gmail.com>}
 */
class SubscriptionRepositoryTest extends Specification {

    SubscriptionRepository repository

    def setup() {
        repository = new SubscriptionRepository()
    }

    def "repository throws an exception when registering a null event"() {
        when:
            repository.register(null)
        then:
            thrown IllegalArgumentException
    }

    def "repository find subscription for event - happy path"() {
        given: "a subscription and an event"
        Event event1 = Mock(Event)
        event1.getName() >> "create user"
        event1.getEmitter() >> "jflu"
        event1.getKind() >> EventKind.ENTITY_CHANGE
        event1.getStatus() >> EventStatus.NEW

        Event event2 = Mock(Event)
        event2.getName() >> "request user"
        event2.getEmitter() >> "jflu"
        event2.getKind() >> EventKind.REQUEST
        event2.getStatus() >> EventStatus.NEW

        Subscription forEvent1 = new Subscription(
            SubscriptionQuery.exactMatchWith(event1),
            "for event 1",
            {event -> println event})
        Subscription forEvent2 = new Subscription(
            SubscriptionQuery.exactMatchWith(event2),
            "for event 2",
            {event -> println event})
        Subscription forAll = new Subscription(
                SubscriptionQuery.thatMatchAllEvents(),
                "all events",
                {event -> println event})
        repository.register(forEvent1)
        repository.register(forEvent2)
        repository.register(forAll)

        when: "finding a subscription for these events"
        Collection<Subscription> subscriptionsForEvent1 = repository.findAllSubscriptionsFor(event1)
        Collection<Subscription> subscriptionsForEvent2 = repository.findAllSubscriptionsFor(event2)

        then: "the subscription matches the initial one"
        subscriptionsForEvent1.size() == 2
        subscriptionsForEvent1.contains forEvent1
        subscriptionsForEvent1.contains forAll
        subscriptionsForEvent2.size() == 2
        subscriptionsForEvent2.contains forEvent2
        subscriptionsForEvent2.contains forAll
    }

    def "allQueriesThatMatch - happy path"() {
        given: "an event"
        Event event = Mock(Event)
        event.getName() >> "create user"
        event.getEmitter() >> "jflu"
        event.getKind() >> EventKind.ENTITY_CHANGE
        event.getStatus() >> EventStatus.NEW

        when:
        Collection<SubscriptionQuery> queries = SubscriptionQuery.allQueriesThatMatch(event)

        then:
        queries.size() == 16
    }
}
