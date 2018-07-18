package be.looorent.jflu

import com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Specification

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TimestampSerializerSpec extends Specification {
    ObjectMapper jsonMapper = Configuration.instance.defaultJsonMapper

    def "serializing a timestamp uses ISO-8601"() {
        given:
        def timestamp = LocalDateTime.of(2018, 07, 19, 12, 9, 54)

        when:
        def timestampAsString = jsonMapper.writeValueAsString(timestamp)

        then:
        timestampAsString == "\"2018-07-19T12:09:54\""
    }

    def "serializing a timestamp is bidirectionnal"() {
        given:
        def expectedTimestamp = LocalDateTime.of(2018, 07, 19, 12, 9, 54)
        def timestampAsString = jsonMapper.writeValueAsString(expectedTimestamp)

        when:
        def actualTimestamp = jsonMapper.readValue(timestampAsString, LocalDateTime)

        then:
        actualTimestamp == expectedTimestamp
    }

    def "deserializing a timestamp with timezone works fine"() {
        given:
        def expectedTimestamp = LocalDateTime.of(2018, 07, 19, 12, 9, 54)
        def timestampAsString = "\"2018-07-19T12:09:54Z\""

        when:
        def actualTimestamp = jsonMapper.readValue(timestampAsString, LocalDateTime)

        then:
        actualTimestamp == expectedTimestamp
    }

    def "deserializing a Ruby Time.now.utc works fine"() {
        given:
        def expectedTimestamp = LocalDateTime.of(2018, 07, 17, 11, 22, 39)
        def timestampAsString = "\"2018-07-17 11:22:39 UTC\""

        when:
        def actualTimestamp = LocalDateTime.parse(timestampAsString, DateTimeFormatter.ofPattern("\"yyyy-MM-dd HH:mm:ss 'UTC'\""))

        then:
        actualTimestamp == expectedTimestamp
    }

    def "deserializing a Ruby Time.now in utc works fine"() {
        given:
        def expectedTimestamp = LocalDateTime.of(2018, 07, 17, 11, 22, 39)
        def timestampAsString = "\"2018-07-17 11:22:39 +0000\""

        when:
        def actualTimestamp = LocalDateTime.parse(timestampAsString, DateTimeFormatter.ofPattern("\"yyyy-MM-dd HH:mm:ss X\""))

        then:
        actualTimestamp == expectedTimestamp
    }

    def "deserializing a localized Ruby Time.now works fine"() {
        given:
        def expectedTimestamp = LocalDateTime.of(2018, 07, 17, 11, 22, 39)
        def timestampAsString = "\"2018-07-17 11:22:39 +0200\""

        when:
        def actualTimestamp = LocalDateTime.parse(timestampAsString, DateTimeFormatter.ofPattern("\"yyyy-MM-dd HH:mm:ss X\""))

        then:
        actualTimestamp == expectedTimestamp
    }
}
