package be.looorent.jflu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Arrays.asList;
import static java.util.Optional.empty;
import static java.util.Optional.of;

// TODO This should be merged with TimestampConverter
public class OffsetTimestampConverter {
    private static final Logger LOG = LoggerFactory.getLogger(OffsetTimestampConverter.class);
    private static final List<DateTimeFormatter> DATE_FORMATS = asList(
            ofPattern("yyyy-MM-dd HH:mm:ss XXX"),
            ofPattern("yyyy-MM-dd HH:mm:ss X"),
            ofPattern("yyyy-MM-dd HH:mm:ss"),
            ISO_DATE_TIME
    );

    private OffsetTimestampConverter() {
        // Only static methods
    }

    public static Optional<LocalDateTime> convertToLocalDateTimeInUtc(String dateAsString) {
        if (dateAsString == null) {
            return empty();
        } else {
            String sanitized = dateAsString.replace("UTC", "Z");
            Optional<LocalDateTime> parsed = DATE_FORMATS.stream()
                    .map(formatter -> parseDate(formatter, sanitized))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(time -> time.atZoneSameInstant(UTC))
                    .map(ZonedDateTime::toLocalDateTime)
                    .findFirst();
            if (parsed.isPresent()) {
                return parsed;
            } else {
                LOG.warn("Impossible to parse {}", dateAsString);
                return TimestampConverter.convertToLocalDateTime(convertToIso(dateAsString));
            }
        }
    }

    private static String convertToIso(String dateAsString) {
        StringBuilder builder = new StringBuilder(dateAsString);
        if (builder.length() > 11 && ' ' == builder.charAt(10)) {
            builder.setCharAt(10, 'T');
        }
        return builder.toString().replace("UTC", "Z").replaceAll(" ", "");
    }

    private static Optional<OffsetDateTime> parseDate(DateTimeFormatter formatter, String dateAsString) {
        try {
            return of(OffsetDateTime.parse(dateAsString, formatter));
        } catch (Exception e) {
            return empty();
        }
    }
}
