package be.looorent.jflu;

import be.looorent.jflu.subscriber.EventMappingKind;
import be.looorent.jflu.subscriber.EventMappingStatus;

/**
 * Create a routing key that can map one or multiple events in a broker using a topic-based communication model.
 * @author Lorent Lempereur <lorent.lempereur.dev@gmail.com>
 */
public class RoutingKeyBuilder {

    private static final String SEPARATOR = ".";
    private static final String WILDCARD = "*";

    private String status;
    private String kind;
    private String emitter;
    private String name;

    private RoutingKeyBuilder() {
        status = WILDCARD;
        kind = WILDCARD;
        emitter = WILDCARD;
        name = WILDCARD;
    }

    public static final RoutingKeyBuilder create() {
        return new RoutingKeyBuilder();
    }

    public String build() {
        StringBuilder builder = new StringBuilder();
        builder.append(status);
        builder.append(SEPARATOR);
        builder.append(emitter);
        builder.append(SEPARATOR);
        builder.append(kind);
        builder.append(SEPARATOR);
        builder.append(name);
        return builder.toString();
    }

    public RoutingKeyBuilder withStatus(EventMappingStatus status) {
        if (status != null && status != EventMappingStatus.ALL) {
            this.status = status.name().toLowerCase();
        }
        return this;
    }

    public RoutingKeyBuilder withStatus(EventStatus status) {
        if (status != null) {
            this.status = status.name().toLowerCase();
        }
        return this;
    }

    public RoutingKeyBuilder withEmitter(String emitter) {
        if (emitter != null && !emitter.isEmpty()) {
            this.emitter = emitter;
        }
        return this;
    }

    public RoutingKeyBuilder withKind(EventMappingKind kind) {
        if (kind != null && kind != EventMappingKind.ALL) {
            this.kind = kind.name().toLowerCase();
        }
        return this;
    }

    public RoutingKeyBuilder withKind(EventKind kind) {
        if (kind != null) {
            this.kind = kind.name().toLowerCase();
        }
        return this;
    }

    public RoutingKeyBuilder withName(String name) {
        if (name != null && !name.isEmpty()) {
            this.name = name;
        }
        return this;
    }
}
