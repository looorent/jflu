package be.looorent.jflu

import be.looorent.jflu.subscriber.EventMappingKind
import be.looorent.jflu.subscriber.EventMappingStatus
import spock.lang.Specification


/**
 * @author Lorent Lempereur {@literal <lorent.lempereur.dev@gmail.com>}
 */
class RoutingKeyBuilderTest extends Specification {

    def "default routing key is *.*.*.*"() {
        RoutingKeyBuilder.create().build() == "*.*.*.*"

        RoutingKeyBuilder.create()
                .withEmitter(null)
                .withKind(null)
                .withName(null)
                .withStatus("")
                .build() == "*.*.*.*"
    }

    def "create routing key from events works"() {
        given: "a set of events properties"
        String emitterGoogle = "google"
        String emitterFacebook = "facebook"
        EventKind entityChange = EventKind.ENTITY_CHANGE
        EventKind request = EventKind.REQUEST
        String friends = "search for friends"
        String restaurants = "create restaurant"
        EventStatus newStatus = EventStatus.NEW
        EventStatus replayedStatus = EventStatus.REPLAYED

        when: "the events are converted to routing keys"
        then: "the routing keys are well-constructed"
        RoutingKeyBuilder.create()
                .withEmitter(emitterGoogle)
                .withKind(request)
                .withName(friends)
                .withStatus(newStatus)
                .build() == "new.google.request.search for friends"

        RoutingKeyBuilder.create()
                .withEmitter(emitterFacebook)
                .withKind(request)
                .withName(friends)
                .withStatus(replayedStatus)
                .build() == "replayed.facebook.request.search for friends"

        RoutingKeyBuilder.create()
                .withEmitter(emitterFacebook)
                .withKind(entityChange)
                .withName(restaurants)
                .withStatus(newStatus)
                .build() == "new.facebook.entity_change.create restaurant"
    }

    def "create routing key from mapping works"() {
        RoutingKeyBuilder.create()
                .withEmitter("google")
                .withKind(EventMappingKind.ALL)
                .withName("search for friends")
                .withStatus("status")
                .build() == "new.google.*.search for friends"

        RoutingKeyBuilder.create()
                .withEmitter("")
                .withKind("")
                .withStatus(EventMappingStatus.REPLAYED)
                .build() == "replayed.*.*.*"

        RoutingKeyBuilder.create()
                .withEmitter("tripavisor")
                .withKind(EventMappingKind.REQUEST)
                .withName("search for things to do")
                .withStatus(EventMappingStatus.NEW)
                .build() == "new.tripavisor.request.search for things to do"
    }
}
