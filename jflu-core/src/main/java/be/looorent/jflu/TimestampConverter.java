package be.looorent.jflu;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static java.time.format.DateTimeFormatter.*;
import static java.util.Arrays.asList;
import static java.util.Optional.*;

public class TimestampConverter {
    private static final List<DateTimeFormatter> DATETIME_FORMATS = asList(
            ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'"),
            ofPattern("yyyy-MM-dd HH:mm:ss X"),
            ISO_DATE_TIME
    );

    private TimestampConverter() {
        // Only static methods
    }

    public static Optional<LocalDateTime> convertToLocalDateTime(String dateAsString) {
        return DATETIME_FORMATS.stream()
                .map(formatter -> parseDatetime(formatter, dateAsString))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }

    public static Optional<LocalDate> convertToLocalDate(String dateAsString) {
        return ofNullable(dateAsString)
                .flatMap(TimestampConverter::parseDate);
    }

    private static Optional<LocalDate> parseDate(String dateAsString) {
        try {
            return of(LocalDate.parse(dateAsString, ISO_LOCAL_DATE));
        } catch (Exception e) {
            return empty();
        }
    }

    private static Optional<LocalDateTime> parseDatetime(DateTimeFormatter formatter, String dateAsString) {
        try {
            return of(LocalDateTime.parse(dateAsString, formatter));
        } catch (Exception e) {
            return empty();
        }
    }
}
