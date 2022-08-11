package be.looorent.jflu.entity;

import be.looorent.jflu.Payload;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static be.looorent.jflu.Payload.nullValue;
import static java.util.Optional.ofNullable;

public class EntityChange {

    private static final int BEFORE_VALUE_INDEX = 0;
    private static final int AFTER_VALUE_INDEX = 1;

    @JsonValue
    private List<Payload> changes;

    @JsonCreator
    public EntityChange(List<Payload> changes) {
        if(changes.size() != 2) {
            throw new IllegalArgumentException("Changes must have two elements");
        }
        this.changes = changes;
    }

    public <T> Optional<T> beforeValue(Class<T> clazz) {
        return selectValue(BEFORE_VALUE_INDEX).get(clazz);
    }

    public <T> Optional<T> afterValue(Class<T> clazz) {
        return selectValue(AFTER_VALUE_INDEX).get(clazz);
    }

    private Payload selectValue(int index) {
        return ofNullable(changes.get(index)).orElse(nullValue());
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        EntityChange that = (EntityChange) other;
        return Objects.equals(changes, that.changes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(changes);
    }

    @Override
    public String toString() {
        return changes.toString();
    }

    public List<Payload> getChanges() {
        return changes;
    }
}
