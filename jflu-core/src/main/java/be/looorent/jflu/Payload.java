package be.looorent.jflu;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class Payload {

    private Object payload;

    @JsonCreator
    public Payload(Object payload) {
        this.payload = payload;
    }

    public <T> T get(Class<T> clazz) {
        if(payload == null) {
            return null;
        }
        if(clazz == null) {
            throw new IllegalArgumentException("Class must be present in order to cast payload");
        }
        Object valueToCast = payload;
        if(clazz.equals(LocalDateTime.class)) {
            Optional<LocalDateTime> date = TimestampConverter.convertToLocalDateTime(String.valueOf(payload));
            if(date.isPresent()) {
                valueToCast = date.get();
            }
        } else if (clazz.equals(UUID.class)) {
            valueToCast = UUID.fromString(String.valueOf(payload));
        }
        return clazz.cast(valueToCast);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payload value = (Payload) o;
        return Objects.equals(payload, value.payload);
    }

    @Override
    public int hashCode() {
        return Objects.hash(payload);
    }

    @Override
    public String toString() {
        return Objects.toString(payload, "null");
    }

    public static Payload nullValue() {
        return new Payload(null);
    }

    @JsonValue
    public Object getPayload() {
        return payload;
    }
}
