package be.looorent.jflu;

import be.looorent.jflu.entity.EntityData;
import be.looorent.jflu.request.RequestData;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * In contrast with {@link EventMetadata}, this interface must be implemented
 * to represent how an event is filled with data.
 * @author Lorent Lempereur {@literal <lorent.lempereur.dev@gmail.com>}
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = EntityData.class, name = "entity"),
        @JsonSubTypes.Type(value = RequestData.class, name = "request")
})
public interface EventData {
}
