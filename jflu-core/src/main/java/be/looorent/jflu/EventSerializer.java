package be.looorent.jflu;

import be.looorent.jflu.entity.EntityData;
import be.looorent.jflu.manual.ManualData;
import be.looorent.jflu.request.RequestData;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import static be.looorent.jflu.EventKind.MANUAL;
import static be.looorent.jflu.TimestampConverter.convertToLocalDateTime;
import static java.util.Optional.of;

public class EventSerializer {

    static class TimestampDeserializer extends JsonDeserializer<LocalDateTime> {

        @Override
        public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            Optional<LocalDateTime> date = convertToLocalDateTime(parser.getValueAsString());
            return date.isPresent() ? date.get() : parser.readValueAs(LocalDateTime.class);
        }

    }

    static class EventDataDeserializer extends JsonDeserializer<EventData> {

        @Override
        public EventData deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            EventKind kind = of(parser.getParsingContext())
                    .map(JsonStreamContext::getParent)
                    .map(JsonStreamContext::getCurrentValue)
                    .map(EventMetadata.class::cast)
                    .map(EventMetadata::getKind)
                    .orElse(MANUAL);

            switch (kind) {
                case ENTITY_CHANGE:
                    return parser.readValueAs(EntityData.class);
                case REQUEST:
                    return parser.readValueAs(RequestData.class);
                case MANUAL:
                default:
                    return parser.readValueAs(ManualData.class);
            }
        }
    }
}
