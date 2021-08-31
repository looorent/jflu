package be.looorent.jflu

import spock.lang.Specification

import java.time.LocalDateTime
import java.time.Month

class OffsetTimestampConverterSpec extends Specification {

    def "#convertToLocalDateTimeInUtc(null) returns empty()"() {
        when:
        def timeInUtc = OffsetTimestampConverter.convertToLocalDateTimeInUtc(null)

        then:
        timeInUtc.isEmpty()
    }

    def "#convertToLocalDateTimeInUtc(date without offset) returns the date in utc"() {
        when:
        def timeInUtc = OffsetTimestampConverter.convertToLocalDateTimeInUtc("2022-03-30 06:48:29")

        then:
        timeInUtc.isPresent()
        timeInUtc.get() == LocalDateTime.of(2022, Month.MARCH, 30, 6, 48, 29)
    }

    def "#convertToLocalDateTimeInUtc(date in utc) returns the date in utc"() {
        when:
        def timeInUtc = OffsetTimestampConverter.convertToLocalDateTimeInUtc("2022-03-30 06:48:29 UTC")

        then:
        timeInUtc.isPresent()
        timeInUtc.get() == LocalDateTime.of(2022, Month.MARCH, 30, 6, 48, 29)
    }

    def "#convertToLocalDateTimeInUtc(date in Europe) returns the date in utc at the same instant"() {
        when:
        def timeInUtc = OffsetTimestampConverter.convertToLocalDateTimeInUtc("2022-03-30 06:48:29 +02:00")

        then:
        timeInUtc.isPresent()
        timeInUtc.get() == LocalDateTime.of(2022, Month.MARCH, 30, 4, 48, 29)
    }
}
