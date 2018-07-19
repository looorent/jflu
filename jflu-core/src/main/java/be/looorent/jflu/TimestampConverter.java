package be.looorent.jflu;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Arrays.asList;
import static java.util.Optional.empty;
import static java.util.Optional.of;

public class TimestampConverter {

    private static final List<DateTimeFormatter> DATE_FORMATS = asList(
            ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'"),
            ofPattern("yyyy-MM-dd HH:mm:ss X"),
            ISO_DATE_TIME
    );

    private TimestampConverter() {
        // Only static methods
    }

    public static Optional<LocalDateTime> convertToLocalDateTime(String dateAsString) {

        return DATE_FORMATS.stream()
                .map(formatter -> parseDate(formatter, dateAsString))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }

    private static Optional<LocalDateTime> parseDate(DateTimeFormatter formatter, String dateAsString) {
        try {
            return of(LocalDateTime.parse(dateAsString, formatter));
        } catch (Exception e) {
            return empty();
        }
    }
}
