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
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static be.looorent.jflu.EventKind.MANUAL;

public class EventSerializer {

    static class TimestampDeserializer extends JsonDeserializer<LocalDateTime> {

        private static DateTimeFormatter[] DATE_FORMATS = new DateTimeFormatter[]{
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss X")
        };

        @Override
        public LocalDateTime deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
            String dateAsString = p.getValueAsString();

            LocalDateTime date;
            for(DateTimeFormatter formatter : DATE_FORMATS) {
                date = parseDate(formatter, dateAsString);
                if (date != null) {
                    return date;
                }
            }
            return p.readValueAs(LocalDateTime.class);
        }

        private LocalDateTime parseDate(DateTimeFormatter formatter, String dateAsString) {
            try {
                return LocalDateTime.parse(dateAsString, formatter);
            } catch (Exception e) {
                return null;
            }
        }
    }

    static class EventDataDeserializer extends JsonDeserializer<EventData> {

        @Override
        public EventData deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
            EventKind kind = Optional.of(p.getParsingContext())
                    .map(JsonStreamContext::getParent)
                    .map(JsonStreamContext::getCurrentValue)
                    .map(v -> (EventMetadata)v)
                    .map(EventMetadata::getKind)
                    .orElse(MANUAL);

            switch (kind) {
                case ENTITY_CHANGE:
                    return p.readValueAs(EntityData.class);
                case REQUEST:
                    return p.readValueAs(RequestData.class);
                case MANUAL:
                default:
                    return p.readValueAs(ManualData.class);
            }
        }
    }
}
