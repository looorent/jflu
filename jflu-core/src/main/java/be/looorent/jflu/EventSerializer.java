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
import java.util.List;
import java.util.Optional;

import static be.looorent.jflu.EventKind.MANUAL;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Arrays.asList;
import static java.util.Optional.empty;
import static java.util.Optional.of;

public class EventSerializer {

    static class TimestampDeserializer extends JsonDeserializer<LocalDateTime> {

        private static final List<DateTimeFormatter> DATE_FORMATS = asList(
            ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'"),
            ofPattern("yyyy-MM-dd HH:mm:ss X")
        );

        @Override
        public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            String dateAsString = parser.getValueAsString();

            Optional<LocalDateTime> date = DATE_FORMATS.stream()
                        .map(formatter -> parseDate(formatter, dateAsString))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .findFirst();

            return date.isPresent() ? date.get() : parser.readValueAs(LocalDateTime.class);
        }

        private Optional<LocalDateTime> parseDate(DateTimeFormatter formatter, String dateAsString) {
            try {
                return of(LocalDateTime.parse(dateAsString, formatter));
            } catch (Exception e) {
                return empty();
            }
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
